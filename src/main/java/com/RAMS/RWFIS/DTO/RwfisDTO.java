package com.RAMS.RWFIS.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RwfisDTO {
    private Long id;
    private double startChainageKm;
    private double endChainageKm;
    private String crossSectionLocation;
    private double offsetM;
    private String feature;
    private String materialType;
    private String featureCondition;
    private boolean safetyHazard;
    private double latitude;
    private double longitude;
    private double altitude;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate surveyDate;
    private String remarks;
}
