package com.mmm.util.location.locationscraper.transform;

import com.mmm.util.location.locationscraper.data.entity.Region;
import com.mmm.util.location.locationscraper.loader.dto.LocationDto;

public class LocationTransform {
    public Region toRegion(LocationDto locationDto) {
        Region region = new Region();
        region.setName(locationDto.getRegion());
        region.setCyrillicName(locationDto.getCyrillicRegion());
        return region;
    }
}
