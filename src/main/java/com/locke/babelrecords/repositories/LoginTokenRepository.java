package com.locke.babelrecords.repositories;

import com.locke.babelrecords.models.LoginToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginTokenRepository extends MongoRepository<LoginToken, ObjectId> {
    public Optional<LoginToken> findByUserId(String userId);
    public void deleteByUserId(String userId);
}
