package com.mmm.util.location.locationscraper.service.impl;

import com.mmm.util.location.locationscraper.data.LocalityRepository;
import com.mmm.util.location.locationscraper.data.entity.Locality;
import com.mmm.util.location.locationscraper.loader.CSVLoader;
import com.mmm.util.location.locationscraper.loader.dto.PostIndexLocationDto;
import com.mmm.util.location.locationscraper.service.LocationProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service("postIndexProcessService")
@Slf4j
public class PostIndexProcessServiceImpl implements LocationProcessService {
    private static final String POSTAL_INDEX_FILE_NAME = "post-indexes-ua.txt";

    private final CSVLoader csvLoader;
    private final LocalityRepository localityRepository;

    public PostIndexProcessServiceImpl(CSVLoader csvLoader, LocalityRepository localityRepository) {
        this.csvLoader = csvLoader;
        this.localityRepository = localityRepository;
    }

    @Override
    public void processAndSave() {
        List<PostIndexLocationDto> postIndexLocationDtos = readPostIndexData();
        postIndexLocationDtos.forEach(postIndexLocationDto -> {
            Optional<Locality> byPostalCode = localityRepository.findByPostalCode(trimZero(postIndexLocationDto.getPostalCode()));
            byPostalCode.ifPresent(locality -> {
                Optional<Locality> localityWithPostalCodes = localityRepository.findById(locality.getId(), 1);
                locality.setApproximateSize(localityWithPostalCodes.get().getPostIndices().size());
                if (postIndexLocationDto.getCoordinateAccuracy() > 3) {
                    locality.setLatitude(postIndexLocationDto.getLatitude());
                    locality.setLongitude(postIndexLocationDto.getLongitude());
                }
                localityRepository.save(locality);
            });
        });
        log.info("Postal index processing completed");
    }

    private List<PostIndexLocationDto> readPostIndexData() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(POSTAL_INDEX_FILE_NAME);
        if (inputStream == null) {
            log.warn("Resource is not available");
        }
        List<PostIndexLocationDto> postIndexLocationDtos = csvLoader.localPostIndexesFromStream(inputStream);
        log.info("Loaded {} records", postIndexLocationDtos.size());
        return postIndexLocationDtos;
    }

    private String trimZero(String postalCode) {
        if (postalCode.startsWith("0")) {
            return trimZero(postalCode.substring(1));
        } else {
            return postalCode;
        }
    }
}
