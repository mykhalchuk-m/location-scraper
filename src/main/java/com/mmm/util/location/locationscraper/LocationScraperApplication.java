package com.mmm.util.location.locationscraper;

import com.mmm.util.location.locationscraper.loader.CSVLoader;
import com.mmm.util.location.locationscraper.service.LocationProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class LocationScraperApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationScraperApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(@Qualifier("locationProcessService") LocationProcessService locationProcessService,
                                               @Qualifier("postIndexProcessService") LocationProcessService postIndexService,
                                               @Qualifier("postVPZAddressProcessService") LocationProcessService postVPZAddressProcessService,
                                               CSVLoader csvLoader) {
        return args -> {
//            locationProcessService.processAndSave();
//            postVPZAddressProcessService.processAndSave();
//            postIndexService.processAndSave();
        };
    }
}