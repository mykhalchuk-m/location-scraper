package com.mmm.util.location.locationscraper.loader.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LocationDto {
    private String region;
    private String district;
    private String locality;
    private String postIndex;
    private String street;
    private List<String> buildingNumber;
    private String cyrillicRegion;
    private String cyrillicDistrict;
    private String cyrillicLocality;
    private String cyrillicStreet;
}
