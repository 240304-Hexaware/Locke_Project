package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.MetaTag;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaTagRepository extends MongoRepository<MetaTag, ObjectId> {
}
