package com.locke.babelrecords.models;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Date;

@Document("loginTokens")
public class LoginToken {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Field(name = "userId")
    private String userId;

    @Field(name = "token")
    private String token;

    @Indexed(expireAfterSeconds = 30)
    @CreatedDate
    private Date createdAt;

    public LoginToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
        // this.deleteAt = LocalDateTime.now().plusSeconds(30);
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
