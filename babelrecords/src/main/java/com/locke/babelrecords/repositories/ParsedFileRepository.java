package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.ParsedFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParsedFileRepository extends MongoRepository<ParsedFile, String> {
}
