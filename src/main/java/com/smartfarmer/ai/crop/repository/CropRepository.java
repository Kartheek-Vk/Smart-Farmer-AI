package com.smartfarmer.ai.crop.repository;

import com.smartfarmer.ai.crop.entity.Crop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CropRepository extends JpaRepository<Crop, UUID> {
    Page<Crop> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
