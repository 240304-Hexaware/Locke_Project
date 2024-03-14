package com.locke.babelrecords.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document
public class MetaTag {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    @Field
    private String userId;
    @CreatedDate
    private Date createdAt;
    @Field
    private String fileName;
    @Field
    private String specFileId;
    @Field
    private int recordsCreated;

    public MetaTag(String userId, String fileName, String specFileId, int recordsCreated) {
        this.userId = userId;
        this.fileName = fileName;
        this.specFileId = specFileId;
        this.recordsCreated = recordsCreated;
    }

    public String getUserId() {
        return userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSpecFileId() {
        return specFileId;
    }

    public int getRecordsCreated() {
        return recordsCreated;
    }
}
