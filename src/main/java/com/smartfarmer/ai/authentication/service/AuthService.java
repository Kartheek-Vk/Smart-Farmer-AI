package com.smartfarmer.ai.authentication.service;

import com.smartfarmer.ai.admin.service.AuditLogService;
import com.smartfarmer.ai.authentication.dto.*;
import com.smartfarmer.ai.authentication.entity.RefreshToken;
import com.smartfarmer.ai.authentication.entity.VerificationToken;
import com.smartfarmer.ai.authentication.repository.RefreshTokenRepository;
import com.smartfarmer.ai.authentication.repository.VerificationTokenRepository;
import com.smartfarmer.ai.common.enums.TokenType;
import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.exception.BusinessException;
import com.smartfarmer.ai.exception.DuplicateResourceException;
import com.smartfarmer.ai.security.CurrentUserService;
import com.smartfarmer.ai.security.JwtTokenProvider;
import com.smartfarmer.ai.user.entity.Role;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.repository.RoleRepository;
import com.smartfarmer.ai.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.Random;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final AuditLogService auditLogService;
    private final CurrentUserService currentUserService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       VerificationTokenRepository verificationTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       AuthenticationManager authenticationManager,
                       AuditLogService auditLogService,
                       CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.auditLogService = auditLogService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        // status defaults to ACTIVE
        user = userRepository.save(user);

        Role farmerRole = roleRepository.findByName(UserRole.FARMER)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(UserRole.FARMER);
                    r.setDescription("Farmer role");
                    return roleRepository.save(r);
                });
        user.getRoles().add(farmerRole);
        userRepository.save(user);

        String accessToken = tokenProvider.createAccessToken(user.getId().toString(), Collections.singleton(UserRole.FARMER));
        String refreshToken = tokenProvider.createRefreshToken(user.getId().toString());
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setToken(refreshToken);
        rt.setExpiresAt(Instant.now().plusMillis(tokenProvider.getRefreshTokenValidityMs()));
        refreshTokenRepository.save(rt);
        
        auditLogService.log("USER_REGISTERED", "User", user.getId().toString(), "User registered with email: " + user.getEmail());
        
        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            throw new BusinessException("Invalid credentials");
        }
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("User not found"));
        String accessToken = tokenProvider.createAccessToken(user.getId().toString(), user.getRolesAsEnumSet());
        String refreshToken = tokenProvider.createRefreshToken(user.getId().toString());
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setToken(refreshToken);
        rt.setExpiresAt(Instant.now().plusMillis(tokenProvider.getRefreshTokenValidityMs()));
        refreshTokenRepository.save(rt);
        
        auditLogService.log("USER_LOGGED_IN", "User", user.getId().toString(), "User logged in");
        
        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        if (!tokenProvider.validateToken(request.refreshToken())) {
            throw new BusinessException("Invalid refresh token");
        }
        
        RefreshToken rt = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException("Refresh token not found"));
                
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("Refresh token expired or revoked");
        }
        
        rt.setRevoked(true);
        refreshTokenRepository.save(rt);
        
        User user = rt.getUser();
        String accessToken = tokenProvider.createAccessToken(user.getId().toString(), user.getRolesAsEnumSet());
        String newRefreshToken = tokenProvider.createRefreshToken(user.getId().toString());
        
        RefreshToken newRt = new RefreshToken();
        newRt.setUser(user);
        newRt.setToken(newRefreshToken);
        newRt.setExpiresAt(Instant.now().plusMillis(tokenProvider.getRefreshTokenValidityMs()));
        refreshTokenRepository.save(newRt);
        
        return new AuthResponse(accessToken, newRefreshToken);
    }
    
    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByToken(request.refreshToken()).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }
    
    @Transactional(readOnly = true)
    public AuthUserResponse getCurrentUser() {
        User user = currentUserService.currentUser();
        return new AuthUserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getStatus(),
            user.getRolesAsEnumSet()
        );
    }
    
    @Transactional
    public OtpResponse forgotPassword(OtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("User not found"));
                
        String code = generateOtp();
        VerificationToken vt = new VerificationToken();
        vt.setUser(user);
        vt.setType(request.type());
        vt.setCode(code);
        vt.setExpiresAt(Instant.now().plusSeconds(15 * 60)); // 15 mins
        verificationTokenRepository.save(vt);
        
        log.info("OTP generated for forgot password: {}", code);
        
        return new OtpResponse("OTP sent successfully", code);
    }
    
    @Transactional
    public void resetPassword(OtpVerificationRequest request) {
        VerificationToken vt = verificationTokenRepository.findByCodeAndTypeAndUsedFalse(request.code(), request.type())
                .orElseThrow(() -> new BusinessException("Invalid or expired OTP"));
                
        if (vt.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("OTP expired");
        }
        
        if (!vt.getUser().getEmail().equals(request.email())) {
            throw new BusinessException("Invalid user for OTP");
        }
        
        vt.setUsed(true);
        verificationTokenRepository.save(vt);
        
        User user = vt.getUser();
        user.setPasswordHash(passwordEncoder.encode("NewPassword123!"));
        userRepository.save(user);
    }

    @Transactional
    public OtpResponse verifyOtp(OtpVerificationRequest request) {
        VerificationToken vt = verificationTokenRepository.findByCodeAndTypeAndUsedFalse(request.code(), request.type())
                .orElseThrow(() -> new BusinessException("Invalid or expired OTP"));
                
        if (vt.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("OTP expired");
        }
        
        if (!vt.getUser().getEmail().equals(request.email())) {
            throw new BusinessException("Invalid user for OTP");
        }
        
        vt.setUsed(true);
        verificationTokenRepository.save(vt);
        return new OtpResponse("OTP verified successfully", null);
    }

    @Transactional
    public OtpResponse resendOtp(OtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("User not found"));
                
        String code = generateOtp();
        VerificationToken vt = new VerificationToken();
        vt.setUser(user);
        vt.setType(request.type());
        vt.setCode(code);
        vt.setExpiresAt(Instant.now().plusSeconds(15 * 60)); // 15 mins
        verificationTokenRepository.save(vt);
        
        log.info("OTP resent: {}", code);
        
        return new OtpResponse("OTP resent successfully", code);
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
