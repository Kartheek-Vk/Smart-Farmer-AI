package com.smartfarmer.ai.scheme.service;

import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.scheme.dto.SchemeRequest;
import com.smartfarmer.ai.scheme.dto.SchemeResponse;
import com.smartfarmer.ai.scheme.entity.GovernmentScheme;
import com.smartfarmer.ai.scheme.repository.GovernmentSchemeRepository;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class SchemeService {

    private final GovernmentSchemeRepository schemeRepository;

    public SchemeService(GovernmentSchemeRepository schemeRepository) {
        this.schemeRepository = schemeRepository;
    }

    @Transactional(readOnly = true)
    public Page<SchemeResponse> listSchemes(String category, String state, Boolean active, Pageable pageable) {
        Page<GovernmentScheme> schemes;
        if (StringUtils.hasText(category) && StringUtils.hasText(state) && active != null) {
            schemes = schemeRepository.findByCategoryAndStateAndActive(category, state, active, pageable);
        } else if (StringUtils.hasText(category) && active != null) {
            schemes = schemeRepository.findByCategoryAndActive(category, active, pageable);
        } else if (StringUtils.hasText(state) && active != null) {
            schemes = schemeRepository.findByStateAndActive(state, active, pageable);
        } else if (active != null) {
            schemes = schemeRepository.findByActive(active, pageable);
        } else {
            schemes = schemeRepository.findAll(pageable);
        }
        return schemes.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public SchemeResponse getSchemeById(UUID id) {
        return schemeRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Government Scheme not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<SchemeResponse> searchSchemes(String query, Pageable pageable) {
        return schemeRepository.searchSchemes(query, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public SchemeResponse createScheme(SchemeRequest request) {
        GovernmentScheme scheme = new GovernmentScheme();
        apply(scheme, request);
        return mapToResponse(schemeRepository.save(scheme));
    }

    @Transactional
    public SchemeResponse updateScheme(UUID id, SchemeRequest request) {
        GovernmentScheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Government Scheme not found with id: " + id));
        apply(scheme, request);
        return mapToResponse(schemeRepository.save(scheme));
    }

    @Transactional
    public void deleteScheme(UUID id) {
        GovernmentScheme scheme = schemeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Government Scheme not found with id: " + id));
        schemeRepository.delete(scheme);
    }

    private void apply(GovernmentScheme scheme, SchemeRequest request) {
        scheme.setTitle(request.title());
        scheme.setCategory(request.category());
        scheme.setState(request.state());
        scheme.setEligibility(request.eligibility());
        scheme.setActive(Boolean.TRUE.equals(request.active()));
        scheme.setDescription(request.description());
    }

    private SchemeResponse mapToResponse(GovernmentScheme scheme) {
        return new SchemeResponse(
                scheme.getId(),
                scheme.getTitle(),
                scheme.getCategory(),
                scheme.getState(),
                scheme.getEligibility(),
                scheme.isActive(),
                scheme.getDescription(),
                scheme.getCreatedAt()
        );
    }
}
