package com.mmm.util.location.locationscraper.data;

import com.mmm.util.location.locationscraper.data.entity.District;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends Neo4jRepository<District, Long> {
}
