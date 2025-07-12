package com.RAMS.RWFIS.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "RWFIS")
public class RwfisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // primary key
    @Column(name = "Start Chainage (km)")
    private double startChainageKm;
    @Column(name = "End Chainage (km)")
    private double endChainageKm;
    @Column(name = "Cross Section Location")
    private String crossSectionLocation;
    @Column(name = "Offset (m)")
    private double offsetM;
    @Column(name = "Feature")
    private String feature;
    @Column(name = "Material Type")
    private String materialType;
    @Column(name = "Feature Condition")
    private String featureCondition;
    @Column(name = "Safety Hazard (Y/N)")
    private boolean safetyHazard;
    @Column(name = "Latitude")
    private double latitude;
    @Column(name = "Longitude")
    private double longitude;
    @Column(name = "Altitude")
    private double altitude;
    @Column(name = "Survey Date")
    private String surveyDate;
    @Column(name = "Remarks")
    private String remarks;

}
