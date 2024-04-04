package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.Record;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends MongoRepository<Record, String> {

}
