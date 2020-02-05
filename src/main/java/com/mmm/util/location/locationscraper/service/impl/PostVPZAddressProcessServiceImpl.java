package com.mmm.util.location.locationscraper.service.impl;

import com.mmm.util.location.locationscraper.data.PostIndexRepository;
import com.mmm.util.location.locationscraper.data.entity.PostIndex;
import com.mmm.util.location.locationscraper.loader.CSVLoader;
import com.mmm.util.location.locationscraper.loader.dto.VPZAddressDto;
import com.mmm.util.location.locationscraper.service.LocationProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service("postVPZAddressProcessService")
@Slf4j
public class PostVPZAddressProcessServiceImpl implements LocationProcessService {
    private final static String FILE_NAME = "postvpz.xls";

    private final CSVLoader csvLoader;
    private final PostIndexRepository postIndexRepository;

    public PostVPZAddressProcessServiceImpl(CSVLoader csvLoader, PostIndexRepository postIndexRepository) {
        this.csvLoader = csvLoader;
        this.postIndexRepository = postIndexRepository;
    }

    @Override
    public void processAndSave() {
        List<VPZAddressDto> vpzAddressDtos = readData();
        vpzAddressDtos.forEach(vpzAddressDto -> {
            Optional<PostIndex> postIndexOptional = postIndexRepository.findByPostalCode(trimZero(vpzAddressDto.getPostalCode()));
            postIndexOptional.ifPresent(postIndex -> {
                postIndex.setVpzAddress(vpzAddressDto.getAddress());
                postIndexRepository.save(postIndex, 0);
            });
        });
        log.info("Process VPZ addresses completed");
    }

    private List<VPZAddressDto> readData() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
        if (inputStream == null) {
            log.warn("Resource is not available");
        }
        List<VPZAddressDto> vpzAddressDtos = csvLoader.loadVPZAddressesFromStream(inputStream);
        log.info("Loaded {} records", vpzAddressDtos.size());
        return vpzAddressDtos;
    }

    private String trimZero(String postalCode) {
        if (postalCode.startsWith("0")) {
            return trimZero(postalCode.substring(1));
        } else {
            return postalCode;
        }
    }
}
