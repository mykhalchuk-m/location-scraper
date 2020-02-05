package com.mmm.util.location.locationscraper.data;

import com.mmm.util.location.locationscraper.data.entity.Locality;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalityRepository extends Neo4jRepository<Locality, Long> {

    @Query("match (l:Locality)-[:DIVIDED_BY]-(p:PostIndex) where p.postalCode={postalCode} return l")
    Optional<Locality> findByPostalCode(String postalCode);
}
