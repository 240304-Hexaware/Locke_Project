package com.locke.babelrecords.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document("parsedFiles")
public class ParsedFile {
  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field(name = "userId")
  private String userId;

  @Field(name = "name")
  private String name;

  @Field(name = "fields")
  private List<List<FileField>> fields;

  public ParsedFile() {
  }

  public ParsedFile(String userId, String name) {
    this.userId = userId;
    this.name = name;
    this.fields = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
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

  public List<List<FileField>> getRecords() {
    return this.fields;
  }

  public void addRecord(List<FileField> record) {
    this.fields.add(record);
  }

  @Override
  public String toString() {
    return "ParsedFile{" +
        "id='" + id + '\'' +
        ", userId='" + userId + '\'' +
        ", name='" + name + '\'' +
        ", fields=" + fields +
        '}';
  }
}
