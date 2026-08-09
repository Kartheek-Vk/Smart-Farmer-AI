package com.smartfarmer.ai.authentication.repository;

import com.smartfarmer.ai.authentication.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import com.smartfarmer.ai.common.enums.TokenType;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByCodeAndTypeAndUsedFalse(String code, TokenType type);
}
