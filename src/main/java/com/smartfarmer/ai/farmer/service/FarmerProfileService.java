package com.smartfarmer.ai.farmer.service;

import com.smartfarmer.ai.farmer.dto.FarmerProfileResponse;
import com.smartfarmer.ai.farmer.dto.UpdateFarmerProfileRequest;
import com.smartfarmer.ai.farmer.entity.FarmerProfile;
import com.smartfarmer.ai.farmer.repository.FarmerProfileRepository;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FarmerProfileService {

    private final FarmerProfileRepository farmerProfileRepository;

    public FarmerProfileService(FarmerProfileRepository farmerProfileRepository) {
        this.farmerProfileRepository = farmerProfileRepository;
    }

    @Transactional
    public FarmerProfileResponse getOrCreateProfile(User user) {
        return toResponse(loadOrCreate(user));
    }

    @Transactional
    public FarmerProfileResponse updateProfile(User user, UpdateFarmerProfileRequest request) {
        FarmerProfile profile = loadOrCreate(user);
        profile.setExperienceLevel(request.experienceLevel());
        profile.setPrimaryCrop(request.primaryCrop());
        profile.setAddress(request.address());
        return toResponse(farmerProfileRepository.save(profile));
    }

    private FarmerProfile loadOrCreate(User user) {
        return farmerProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    FarmerProfile profile = new FarmerProfile();
                    profile.setUser(user);
                    return farmerProfileRepository.save(profile);
                });
    }

    private FarmerProfileResponse toResponse(FarmerProfile profile) {
        return new FarmerProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getExperienceLevel(),
                profile.getPrimaryCrop(),
                profile.getAddress());
    }
}
