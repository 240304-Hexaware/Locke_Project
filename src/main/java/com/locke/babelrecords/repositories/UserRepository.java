package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

  Optional<User> findById(String id);

  Optional<User> findByUsername(String username);

  void deleteById(String username);
}
