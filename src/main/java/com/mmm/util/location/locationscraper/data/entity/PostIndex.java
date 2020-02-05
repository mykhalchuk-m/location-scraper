package com.mmm.util.location.locationscraper.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

@NodeEntity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class PostIndex {
    @Id
    @GeneratedValue
    private Long id;

    public PostIndex(String postalCode) {
        this.postalCode = postalCode;
    }

    private Double latitude;
    private Double longitude;
    private String vpzAddress;

    private String postalCode;
    private List<AddressRange> addressRanges;
}
