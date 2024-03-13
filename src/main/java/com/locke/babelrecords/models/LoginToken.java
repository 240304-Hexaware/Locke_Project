package com.locke.babelrecords.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.time.LocalTime;

@Document("loginTokens")
@EnableMongoAuditing()
public class LoginToken {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Field(name = "userId")
    private String userId;

    @Field(name = "token")
    private String token;

    @Indexed(name = "deleteAt", expireAfterSeconds = 30)
    @CreatedDate
    private LocalTime createdAt;

    public LoginToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
        this.createdAt = LocalTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
