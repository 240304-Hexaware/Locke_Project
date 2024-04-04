package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.MetaTag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetaTagRepository extends MongoRepository<MetaTag, String> {

  List<MetaTag> findByUserId(String userId);

  List<MetaTag> findByUserIdOrderByCreatedAtDesc(String userId);
}
