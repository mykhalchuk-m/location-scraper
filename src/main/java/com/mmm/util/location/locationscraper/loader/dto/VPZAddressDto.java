package com.mmm.util.location.locationscraper.loader.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VPZAddressDto {
    private String postalCode;
    private String address;
}
