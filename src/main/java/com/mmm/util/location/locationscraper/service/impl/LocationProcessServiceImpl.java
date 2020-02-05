package com.mmm.util.location.locationscraper.service.impl;

import com.mmm.util.location.locationscraper.data.AddressRangeRepository;
import com.mmm.util.location.locationscraper.data.CountryRepository;
import com.mmm.util.location.locationscraper.data.DistrictRepository;
import com.mmm.util.location.locationscraper.data.LocalityRepository;
import com.mmm.util.location.locationscraper.data.PostIndexRepository;
import com.mmm.util.location.locationscraper.data.RegionRepository;
import com.mmm.util.location.locationscraper.data.entity.AddressRange;
import com.mmm.util.location.locationscraper.data.entity.Country;
import com.mmm.util.location.locationscraper.data.entity.District;
import com.mmm.util.location.locationscraper.data.entity.Locality;
import com.mmm.util.location.locationscraper.data.entity.PostIndex;
import com.mmm.util.location.locationscraper.data.entity.Region;
import com.mmm.util.location.locationscraper.loader.CSVLoader;
import com.mmm.util.location.locationscraper.loader.dto.LocationDto;
import com.mmm.util.location.locationscraper.service.LocationProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("locationProcessService")
@Slf4j
public class LocationProcessServiceImpl implements LocationProcessService {
    private static final String FILE_NAME = "10-09-2018.csv";

    private final CSVLoader csvLoader;
    private final CountryRepository countryRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final LocalityRepository localityRepository;
    private final PostIndexRepository postIndexRepository;
    private final AddressRangeRepository addressRangeRepository;

    public LocationProcessServiceImpl(CSVLoader csvLoader, CountryRepository countryRepository, RegionRepository regionRepository, DistrictRepository districtRepository, LocalityRepository localityRepository, PostIndexRepository postIndexRepository, AddressRangeRepository addressRangeRepository) {
        this.csvLoader = csvLoader;
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.districtRepository = districtRepository;
        this.localityRepository = localityRepository;
        this.postIndexRepository = postIndexRepository;
        this.addressRangeRepository = addressRangeRepository;
    }

    public void processAndSave() {
        List<LocationDto> locationDtos = readLocationData();
        Map<Region, Map<District, Map<Locality, Map<PostIndex, List<AddressRange>>>>> map = processLocations(locationDtos);
        saveLocations(map);
    }

    private List<LocationDto> readLocationData() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        if (inputStream == null) {
            log.warn("Resource is not available");
        }

        List<LocationDto> locationDtos = csvLoader.loadLocationFromInputStream(inputStream);
        log.info("Loaded {} items", locationDtos.size());

        return locationDtos;
    }

    private Map<Region, Map<District, Map<Locality, Map<PostIndex, List<AddressRange>>>>> processLocations(List<LocationDto> locationDtos) {
        Map<Region, Map<District, Map<Locality, Map<PostIndex, List<AddressRange>>>>> map = locationDtos.stream()
                .collect(Collectors.groupingBy(l -> new Region(l.getRegion(), l.getCyrillicRegion()),
                        Collectors.groupingBy(districtMapper,
                                Collectors.groupingBy(l -> new Locality(l.getLocality(), l.getCyrillicLocality()),
                                        Collectors.groupingBy(l -> new PostIndex(l.getPostIndex()),
                                                Collectors.mapping(l -> new AddressRange(l.getStreet(), l.getCyrillicStreet(), l.getBuildingNumber()),
                                                        Collectors.toList()))))));

        log.info("Mapped {} objects", map.size());
        return map;
    }

    private void saveLocations(Map<Region, Map<District, Map<Locality, Map<PostIndex, List<AddressRange>>>>> map) {
        Set<Region> regions = map.keySet();
        Country ua = new Country();
        ua.setName("Ukraine");
        ua.setCyrillicName("Україна");
        List<Region> regionList = regions.stream()
                .map(region -> {
                    Map<District, Map<Locality, Map<PostIndex, List<AddressRange>>>> districtMap = map.get(region);
                    Set<District> districts = districtMap.keySet();
                    List<District> districtList = districts.stream()
                            .map(district -> {
                                Map<Locality, Map<PostIndex, List<AddressRange>>> localityMap = districtMap.get(district);
                                Set<Locality> localities = localityMap.keySet();
                                List<Locality> localityList = localities.stream()
                                        .map(locality -> {
                                            Map<PostIndex, List<AddressRange>> postIndexes = localityMap.get(locality);
                                            Set<PostIndex> postIndices = postIndexes.keySet();
                                            List<PostIndex> postIndexList = postIndices.stream()
                                                    .map(postIndex -> {
                                                        postIndex.setAddressRanges(postIndexes.get(postIndex).stream().map(addressRangeRepository::save).collect(Collectors.toList()));
                                                        return postIndexRepository.save(postIndex);
                                                    }).collect(Collectors.toList());
                                            locality.setPostIndices(postIndexList);
                                            return localityRepository.save(locality);
                                        })
                                        .collect(Collectors.toList());

                                district.setLocalities(localityList);
                                return districtRepository.save(district);
                            })
                            .collect(Collectors.toList());
                    region.setDistricts(districtList);
                    log.info("Saving region: {}", region.getName());
                    return regionRepository.save(region);
                })
                .collect(Collectors.toList());
        ua.setRegions(regionList);
        log.info("Success processing");

        countryRepository.save(ua);
        log.info("Success persisting");
    }

    private Function<LocationDto, District> districtMapper = (l) -> {
        if (StringUtils.isEmpty(l.getDistrict())) {
            return new District(l.getLocality(), l.getCyrillicLocality());
        } else {
            return new District(l.getDistrict(), l.getCyrillicDistrict());
        }
    };
}
