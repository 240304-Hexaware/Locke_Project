package com.locke.babelrecords.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("specFiles")
public class SpecFile {
  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field(name = "userId")
  private String userId;

  @Field(name = "name")
  private String name;

  @Field(name = "specs")
  private List<SpecField> fields;

  @Field
  private List<String> parsedFileIds;

  @Field(name = "recordIds")
  private List<String> recordIds;

  @Field
  @CreatedDate
  private Date createdAt;

  public SpecFile() {
  }

  public SpecFile(String userId, String name, List<SpecField> fields) {
    this.userId = userId;
    this.name = name;
    this.fields = fields;
    this.recordIds = new ArrayList<>();
    this.parsedFileIds = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getRecordIds() {
    return recordIds;
  }

  public void addRecordId(String id) {
    this.recordIds.add(id);
  }

  public void addRecordIds(List<String> recordIds) {
    this.recordIds.addAll(recordIds);
  }

  public List<SpecField> getFields() {
    return fields;
  }

  public void setFields(List<SpecField> fields) {
    this.fields = fields;
  }

  public List<String> getParsedFileIds() {
    return parsedFileIds;
  }

  public void addParsedFileId(String id) {
    this.parsedFileIds.add(id);
  }

  public void setParsedFileIds(List<String> parsedFileIds) {
    this.parsedFileIds = parsedFileIds;
  }

  public void setRecordIds(List<String> recordIds) {
    this.recordIds = recordIds;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "SpecFile{" +
        "id='" + id + '\'' +
        ", userId='" + userId + '\'' +
        ", name='" + name + '\'' +
        ", specs=" + recordIds +
        '}';
  }
}
