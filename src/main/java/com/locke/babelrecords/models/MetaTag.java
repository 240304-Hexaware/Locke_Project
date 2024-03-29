package com.locke.babelrecords.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
public class MetaTag {
  @MongoId(FieldType.OBJECT_ID)
  private String id;
  @Field
  private String userId;
  @CreatedDate
  private Date createdAt;

  @Field
  private String operation;
  @Field
  private String fileName;
  @Field
  private String fileId;
  @Field
  private List<String> recordsCreated;

  public MetaTag(String userId, String operation, String fileName, String fileId) {
    this.userId = userId;
    this.operation = operation;
    this.fileName = fileName;
    this.fileId = fileId;
    this.recordsCreated = new ArrayList<>();
  }

  public MetaTag(String userId, String operation, String fileName, String fileId, List<String> recordsCreated) {
    this.userId = userId;
    this.operation = operation;
    this.fileName = fileName;
    this.fileId = fileId;
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

  public String getFileId() {
    return fileId;
  }

  public List<String> getRecordsCreated() {
    return recordsCreated;
  }
}
