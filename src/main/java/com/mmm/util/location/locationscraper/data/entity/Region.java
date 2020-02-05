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
public class Region {
    @Id
    @GeneratedValue
    private Long id;

    public Region(String name, String cyrillicName) {
        this.name = name;
        this.cyrillicName = cyrillicName;
    }

    private String name;
    private String cyrillicName;
    @Relationship("DIVIDED_BY")
    private List<District> districts;
}
