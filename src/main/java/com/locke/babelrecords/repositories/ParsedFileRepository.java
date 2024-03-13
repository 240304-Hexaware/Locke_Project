package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.SpecFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParsedFileRepository extends MongoRepository<ParsedFile, ObjectId> {
    Optional<ParsedFile> findById(String id);
    Optional<ParsedFile> findByName(String name);
    List<ParsedFile> findByUserId(String userId);
}
