package com.mmm.util.location.locationscraper.web;

import com.mmm.util.location.locationscraper.data.LocalityRepository;
import com.mmm.util.location.locationscraper.data.entity.Locality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LocationController {

    private final LocalityRepository localityRepository;

    @Autowired
    public LocationController(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
    }

    @GetMapping("/locality")
    public Optional<Locality> getByPostalCode(@RequestParam String postalCode) {
        return localityRepository.findByPostalCode(postalCode);
    }
}
