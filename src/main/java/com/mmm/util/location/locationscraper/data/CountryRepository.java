package com.mmm.util.location.locationscraper.data;

import com.mmm.util.location.locationscraper.data.entity.Country;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends Neo4jRepository<Country, Long> {
}
