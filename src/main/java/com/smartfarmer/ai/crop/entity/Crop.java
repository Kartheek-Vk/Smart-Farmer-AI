package com.smartfarmer.ai.crop.entity;

import com.smartfarmer.ai.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "crops")
public class Crop extends BaseEntity {

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(length = 500)
    private String description;

    @Column(length = 120)
    private String season;
}
