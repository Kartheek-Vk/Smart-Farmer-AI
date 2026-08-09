package com.smartfarmer.ai.authentication.service;

import com.smartfarmer.ai.admin.service.AuditLogService;
import com.smartfarmer.ai.authentication.OtpProperties;
import com.smartfarmer.ai.authentication.dto.AuthResponse;
import com.smartfarmer.ai.authentication.dto.AuthUserResponse;
import com.smartfarmer.ai.authentication.dto.LoginRequest;
import com.smartfarmer.ai.authentication.dto.OtpRequest;
import com.smartfarmer.ai.authentication.dto.OtpResponse;
import com.smartfarmer.ai.authentication.dto.OtpVerificationRequest;
import com.smartfarmer.ai.authentication.dto.RefreshTokenRequest;
import com.smartfarmer.ai.authentication.dto.RegisterRequest;
import com.smartfarmer.ai.authentication.dto.ResetPasswordRequest;
import com.smartfarmer.ai.authentication.entity.RefreshToken;
import com.smartfarmer.ai.authentication.entity.VerificationToken;
import com.smartfarmer.ai.authentication.repository.RefreshTokenRepository;
import com.smartfarmer.ai.authentication.repository.VerificationTokenRepository;
import com.smartfarmer.ai.common.enums.OtpPurpose;
import com.smartfarmer.ai.common.enums.TokenType;
import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.common.enums.UserStatus;
import com.smartfarmer.ai.common.util.TokenHasher;
import com.smartfarmer.ai.exception.BusinessException;
import com.smartfarmer.ai.exception.DuplicateResourceException;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.exception.UnauthorizedException;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.security.JwtTokenProvider;
import com.smartfarmer.ai.user.entity.Role;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.repository.RoleRepository;
import com.smartfarmer.ai.user.repository.UserRepository;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Locale;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Set<UserRole> SELF_ASSIGNABLE_ROLES =
            Set.of(UserRole.FARMER, UserRole.EXPERT, UserRole.DEALER, UserRole.NGO);

    private final SecureRandom secureRandom = new SecureRandom();

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuditLogService auditLogService;
    private final CurrentUserService currentUserService;
    private final OtpProperties otpProperties;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       VerificationTokenRepository verificationTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       AuthenticationManager authenticationManager,
                       AuditLogService auditLogService,
                       CurrentUserService currentUserService,
                       OtpProperties otpProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.auditLogService = auditLogService;
        this.currentUserService = currentUserService;
        this.otpProperties = otpProperties;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already in use");
        }

        UserRole requestedRole = request.role() == null ? UserRole.FARMER : request.role();
        if (!SELF_ASSIGNABLE_ROLES.contains(requestedRole)) {
            throw new BusinessException("Role " + requestedRole + " cannot be self-assigned");
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.getRoles().add(requireRole(requestedRole));
        user = userRepository.save(user);

        auditLogService.log("USER_REGISTERED", "USER", user.getId().toString(), "User registered");
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Invalid email or password");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        auditLogService.log("USER_LOGGED_IN", "USER", user.getId().toString(), "User logged in");
        return issueTokens(user);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        String token = request.refreshToken();
        if (!tokenProvider.validateToken(token) || !tokenProvider.isTokenOfType(token, TokenType.REFRESH)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        RefreshToken stored = refreshTokenRepository.findByTokenHash(TokenHasher.sha256(token))
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (!stored.isActive(Instant.now())) {
            // A revoked token being replayed means the token may have leaked: drop the whole family.
            revokeAllForUser(stored.getUser().getId());
            throw new UnauthorizedException("Refresh token is expired or revoked");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        User user = stored.getUser();
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UnauthorizedException("Account is not active");
        }
        return issueTokens(user);
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByTokenHash(TokenHasher.sha256(request.refreshToken()))
                .ifPresent(stored -> {
                    stored.setRevoked(true);
                    refreshTokenRepository.save(stored);
                    auditLogService.log("USER_LOGGED_OUT", "USER", stored.getUser().getId().toString(), "User logged out");
                });
    }

    @Transactional(readOnly = true)
    public AuthUserResponse getCurrentUser() {
        return toAuthUser(currentUserService.currentUser());
    }

    @Transactional
    public OtpResponse forgotPassword(OtpRequest request) {
        return createOtp(request.email(), OtpPurpose.RESET_PASSWORD, "If the account exists, an OTP has been sent");
    }

    @Transactional
    public OtpResponse resendOtp(OtpRequest request) {
        return createOtp(request.email(), request.purpose(), "If the account exists, an OTP has been sent");
    }

    @Transactional
    public OtpResponse verifyOtp(OtpVerificationRequest request) {
        VerificationToken token = consumeOtp(normalizeEmail(request.email()), request.code(), request.purpose());
        if (token.getPurpose() == OtpPurpose.VERIFY_ACCOUNT) {
            User user = token.getUser();
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
        }
        return new OtpResponse("OTP verified successfully", null);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        VerificationToken token = consumeOtp(normalizeEmail(request.email()), request.code(), OtpPurpose.RESET_PASSWORD);
        User user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        revokeAllForUser(user.getId());
        auditLogService.log("PASSWORD_RESET", "USER", user.getId().toString(), "Password reset via OTP");
    }

    private OtpResponse createOtp(String email, OtpPurpose purpose, String message) {
        String normalized = normalizeEmail(email);
        return userRepository.findByEmail(normalized)
                .map(user -> {
                    String code = generateOtp();
                    VerificationToken token = new VerificationToken();
                    token.setUser(user);
                    token.setPurpose(purpose);
                    token.setCodeHash(TokenHasher.sha256(code));
                    token.setExpiresAt(Instant.now().plusSeconds(otpProperties.ttlSeconds()));
                    verificationTokenRepository.save(token);
                    return new OtpResponse(message, otpProperties.exposeDevCodeInResponse() ? code : null);
                })
                // Do not disclose whether the account exists.
                .orElseGet(() -> new OtpResponse(message, null));
    }

    private VerificationToken consumeOtp(String email, String code, OtpPurpose purpose) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Invalid or expired OTP"));
        VerificationToken token = verificationTokenRepository
                .findByUserIdAndPurposeAndCodeHashAndUsedFalse(user.getId(), purpose, TokenHasher.sha256(code))
                .orElseThrow(() -> new BusinessException("Invalid or expired OTP"));
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("Invalid or expired OTP");
        }
        token.setUsed(true);
        return verificationTokenRepository.save(token);
    }

    private AuthResponse issueTokens(User user) {
        Set<UserRole> roles = user.getRolesAsEnumSet();
        String accessToken = tokenProvider.createAccessToken(user.getId().toString(), roles);
        String refreshToken = tokenProvider.createRefreshToken(user.getId().toString());

        RefreshToken stored = new RefreshToken();
        stored.setUser(user);
        stored.setTokenHash(TokenHasher.sha256(refreshToken));
        stored.setExpiresAt(Instant.now().plus(tokenProvider.refreshTokenValidity()));
        refreshTokenRepository.save(stored);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                tokenProvider.accessTokenValidity().toSeconds(),
                toAuthUser(user));
    }

    private void revokeAllForUser(java.util.UUID userId) {
        var tokens = refreshTokenRepository.findByUserIdAndRevokedFalse(userId);
        tokens.forEach(token -> token.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }

    private Role requireRole(UserRole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + role + " is not configured"));
    }

    private AuthUserResponse toAuthUser(User user) {
        return new AuthUserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getStatus(),
                user.getRolesAsEnumSet());
    }

    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
