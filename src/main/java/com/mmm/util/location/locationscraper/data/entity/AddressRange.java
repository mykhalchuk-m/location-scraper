package com.mmm.util.location.locationscraper.data.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
public class AddressRange {
    @Id
    @GeneratedValue
    private Long id;

    public AddressRange(String street, String cyrillicStreet, List<String> buildingNumber) {
        this.street = street;
        this.cyrillicStreet = cyrillicStreet;
        this.buildingNumbers = buildingNumber;
    }

    private String street;
    private String cyrillicStreet;

    private List<String> buildingNumbers;
}
