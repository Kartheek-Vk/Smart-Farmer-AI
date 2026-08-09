package com.smartfarmer.ai.crop.repository;

import com.smartfarmer.ai.common.enums.CropSeasonStatus;
import com.smartfarmer.ai.crop.entity.CropSeason;
import java.util.Collection;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropSeasonRepository extends JpaRepository<CropSeason, UUID> {

    Page<CropSeason> findByOwnerId(UUID ownerId, Pageable pageable);

    long countByOwnerId(UUID ownerId);

    long countByOwnerIdAndFarmId(UUID ownerId, UUID farmId);

    Page<CropSeason> findByOwnerIdAndFarmId(UUID ownerId, UUID farmId, Pageable pageable);

    Page<CropSeason> findByOwnerIdAndStatus(UUID ownerId, CropSeasonStatus status, Pageable pageable);

    Page<CropSeason> findByOwnerIdAndStatusIn(UUID ownerId, Collection<CropSeasonStatus> statuses, Pageable pageable);
}
