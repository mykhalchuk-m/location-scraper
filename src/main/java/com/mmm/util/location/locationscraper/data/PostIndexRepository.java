package com.mmm.util.location.locationscraper.data;

import com.mmm.util.location.locationscraper.data.entity.PostIndex;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface PostIndexRepository extends Neo4jRepository<PostIndex, Long> {


    Optional<PostIndex> findByPostalCode(String postalCode);
}
