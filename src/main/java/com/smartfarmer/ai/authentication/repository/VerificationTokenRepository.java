package com.smartfarmer.ai.authentication.repository;

import com.smartfarmer.ai.authentication.entity.VerificationToken;
import com.smartfarmer.ai.common.enums.OtpPurpose;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    Optional<VerificationToken> findByUserIdAndPurposeAndCodeHashAndUsedFalse(
            UUID userId, OtpPurpose purpose, String codeHash);
}
