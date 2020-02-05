package com.mmm.util.location.locationscraper.data;

import com.mmm.util.location.locationscraper.data.entity.AddressRange;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRangeRepository extends Neo4jRepository<AddressRange, Long> {
}
