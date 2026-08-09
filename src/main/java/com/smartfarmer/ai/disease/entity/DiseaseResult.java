package com.smartfarmer.ai.disease.entity;

import com.smartfarmer.ai.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "disease_results")
public class DiseaseResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disease_scan_id", nullable = false)
    private DiseaseScan diseaseScan;

    @Column(nullable = false, length = 150)
    private String diseaseName;

    @Column(nullable = false)
    private double confidence;

    @Column(length = 1000)
    private String summary;

    @Column(length = 1000)
    private String recommendation;
}
