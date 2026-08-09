package com.smartfarmer.ai.farm.entity;

import com.smartfarmer.ai.common.enums.AreaUnit;
import com.smartfarmer.ai.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "farm_fields")
public class FarmField extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AreaUnit areaUnit;

    @Column(length = 255)
    private String notes;
}
