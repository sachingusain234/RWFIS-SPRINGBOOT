package com.RAMS.RWFIS.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RsiDTO {
    //private Long id;
    private double startChainageKm;
    private double endChainageKm;
    private String crossSectionLocation;
    //private double offsetM;
    private String feature;
    //private String materialType;
    private boolean safetyHazard;
    private String featureCondition;
//    private double latitude;
//    private double longitude;
//    private double altitude;
//    private String remarks;
    private Long insertedBy;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate lastUpdatedDate;
}
