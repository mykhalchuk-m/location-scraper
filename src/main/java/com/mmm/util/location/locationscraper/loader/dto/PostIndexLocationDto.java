package com.mmm.util.location.locationscraper.loader.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostIndexLocationDto {
    private String countryCode;
    private String postalCode;
    private String localityCyrillicName;
    private String localityName;
    private String districtName;
    private Double latitude;
    private Double longitude;
    private Integer coordinateAccuracy;
}
