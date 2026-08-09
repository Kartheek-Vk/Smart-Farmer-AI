package com.smartfarmer.ai.farm.entity;

import com.smartfarmer.ai.common.enums.AreaUnit;
import com.smartfarmer.ai.common.enums.IrrigationType;
import com.smartfarmer.ai.common.enums.OwnershipType;
import com.smartfarmer.ai.common.enums.SoilType;
import com.smartfarmer.ai.common.model.BaseEntity;
import com.smartfarmer.ai.user.entity.User;
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
@Table(name = "farms")
public class Farm extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String location;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AreaUnit areaUnit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SoilType soilType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IrrigationType irrigationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OwnershipType ownershipType;
}
