package com.mmm.util.location.locationscraper.data;

import com.mmm.util.location.locationscraper.data.entity.Region;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends Neo4jRepository<Region, Long> {
}
