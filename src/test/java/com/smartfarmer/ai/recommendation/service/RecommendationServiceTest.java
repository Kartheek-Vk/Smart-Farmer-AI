package com.smartfarmer.ai.recommendation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.smartfarmer.ai.common.enums.RecommendationStatus;
import com.smartfarmer.ai.farm.service.FarmService;
import com.smartfarmer.ai.integration.ai.AiServiceClient;
import com.smartfarmer.ai.recommendation.dto.CropRecommendationRequest;
import com.smartfarmer.ai.recommendation.dto.CropRecommendationResponse;
import com.smartfarmer.ai.recommendation.dto.RecommendationDecision;
import com.smartfarmer.ai.recommendation.entity.CropRecommendation;
import com.smartfarmer.ai.recommendation.repository.CropRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.FertilizerRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.IrrigationRecommendationRepository;
import com.smartfarmer.ai.recommendation.repository.RecommendationHistoryRepository;
import com.smartfarmer.ai.user.entity.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RecommendationServiceTest {

    @Mock
    private CropRecommendationRepository cropRepository;
    @Mock
    private FertilizerRecommendationRepository fertilizerRepository;
    @Mock
    private IrrigationRecommendationRepository irrigationRepository;
    @Mock
    private RecommendationHistoryRepository historyRepository;
    @Mock
    private AiServiceClient aiServiceClient;
    @Mock
    private FarmService farmService;

    private RecommendationService service() {
        return new RecommendationService(cropRepository, fertilizerRepository, irrigationRepository,
                historyRepository, aiServiceClient, farmService);
    }

    @Test
    void keepsRequestPendingWhenAiProviderIsNotConfigured() {
        when(aiServiceClient.isAvailable()).thenReturn(false);
        when(cropRepository.save(any(CropRecommendation.class))).thenAnswer(invocation -> {
            CropRecommendation saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        CropRecommendationResponse response = service().createCropRecommendation(
                new CropRecommendationRequest(null, "LOAM", "KHARIF", "TROPICAL", "DRIP", "none"),
                user());

        assertThat(response.status()).isEqualTo(RecommendationStatus.PENDING.name());
        assertThat(response.recommendationText()).isNull();
        verify(aiServiceClient, never()).generateCropRecommendation(any());
    }

    @Test
    void storesTheProviderDecisionWhenTheAiServiceAnswers() {
        when(aiServiceClient.isAvailable()).thenReturn(true);
        when(aiServiceClient.generateCropRecommendation(any()))
                .thenReturn(new RecommendationDecision(RecommendationStatus.GENERATED, "Grow millet"));
        when(cropRepository.save(any(CropRecommendation.class))).thenAnswer(invocation -> {
            CropRecommendation saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        CropRecommendationResponse response = service().createCropRecommendation(
                new CropRecommendationRequest(null, "LOAM", "KHARIF", "TROPICAL", "DRIP", "none"),
                user());

        assertThat(response.status()).isEqualTo(RecommendationStatus.GENERATED.name());
        assertThat(response.recommendationText()).isEqualTo("Grow millet");
    }

    private User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("farmer@example.com");
        return user;
    }
}
