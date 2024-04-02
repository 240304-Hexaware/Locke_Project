package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.Record;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

}
