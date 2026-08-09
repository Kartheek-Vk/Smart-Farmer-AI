package com.smartfarmer.ai.crop.repository;

import com.smartfarmer.ai.crop.entity.CropSeason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CropSeasonRepository extends JpaRepository<CropSeason, UUID> {
    List<CropSeason> findByCropId(UUID cropId);

    List<CropSeason> findByOwnerIdOrderByCreatedAtDesc(UUID ownerId);
    
    Page<CropSeason> findByOwnerId(UUID ownerId, Pageable pageable);
}
