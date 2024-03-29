package com.locke.babelrecords.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document("record")
public class Record {

  @Field("_id")
  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field("fields")
  private Map<String, Object> fields;

  public Record() {
    this.fields = new HashMap<>();
  }

  public void addField(String name, Object value) {
    this.fields.put(name, value);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Record{" +
        "fields=" + fields +
        '}';
  }
}
