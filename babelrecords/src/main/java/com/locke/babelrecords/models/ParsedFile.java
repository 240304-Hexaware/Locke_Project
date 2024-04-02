package com.locke.babelrecords.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("parsedFiles")
public class ParsedFile {
  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field(name = "name")
  private String name;

  @Field(name = "record")
  private List<String> recordIds;

  @Field
  @CreatedDate
  private Date createdAt;

  public ParsedFile() {
  }

  public ParsedFile(String name) {
    this.name = name;
    this.recordIds = new ArrayList<>();
  }

  public ParsedFile(String name, List<String> recordIds) {
    this.name = name;
    this.recordIds = recordIds;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public List<String> getRecordIds() {
    return this.recordIds;
  }

  public void setRecordIds(List<String> recordIds) {
    this.recordIds = recordIds;
  }

  public void addRecordIds(List<String> recordIds) {
    this.recordIds.addAll(recordIds);
  }

  @Override
  public String toString() {
    return "ParsedFile{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", fields=" + recordIds +
        '}';
  }
}
