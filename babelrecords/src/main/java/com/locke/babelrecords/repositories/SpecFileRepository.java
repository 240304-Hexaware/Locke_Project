package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.SpecFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecFileRepository extends MongoRepository<SpecFile, String> {
  Optional<SpecFile> findByName(String name);

  List<SpecFile> findByUserId(String userId);
}
