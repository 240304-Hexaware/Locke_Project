package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.ParsedFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParsedFileRepository extends MongoRepository<ParsedFile, ObjectId> {
    Optional<ParsedFile> findByName(String name);
}
