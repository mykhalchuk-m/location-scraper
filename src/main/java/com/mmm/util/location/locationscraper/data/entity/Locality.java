package com.mmm.util.location.locationscraper.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Locality {
    @Id
    @GeneratedValue
    private Long id;

    public Locality(String name, String cyrillicName) {
        this.name = name;
        this.cyrillicName = cyrillicName;
    }

    private String name;
    private String cyrillicName;

    private Long popularity;
    private Double latitude;
    private Double longitude;
    private Integer approximateSize;

    @Relationship("DIVIDED_BY")
    private List<PostIndex> postIndices;
}
