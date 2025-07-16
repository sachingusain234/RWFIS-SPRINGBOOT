package com.RAMS.RWFIS.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate surveyDate;
    @Column(name = "Remarks")
    private String remarks;
    @Column(name="isDeleted")
    private boolean deleted ;
    @Column(name="created_by")
    private Long createdBy;
    @Column(name = "created_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdDate;
    @Column(name = "lastupdated_by")
    private Long lastupdatedBy;
    @Column(name = "lastupdated_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate lastupdatedDate;
    @Column(name = "rsi_id")
    private Long rsiId;
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDate.now();
    }
    @PostPersist
    public void postPersist() {
        if (this.createdBy == null) {
            this.createdBy = this.id;
            // You must update the entity to save the createdBy value after ID generation
            // because this method is called after persist but before transaction commit.
        }
    }
}
