package com.mmm.util.location.locationscraper.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
@Data
@EqualsAndHashCode
public class Country {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String cyrillicName;
    @Relationship("DIVIDED_BY")
    private List<Region> regions;
}
