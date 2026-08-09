package com.smartfarmer.ai.farmer.repository;

import com.smartfarmer.ai.farmer.entity.FarmerProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerProfileRepository extends JpaRepository<FarmerProfile, UUID> {

    Optional<FarmerProfile> findByUserId(UUID userId);
}
