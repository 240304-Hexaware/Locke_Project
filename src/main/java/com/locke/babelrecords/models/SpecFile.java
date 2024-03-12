package com.locke.babelrecords.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
    private List<SpecField> specs;

    public SpecFile(String userId, String name, List<SpecField> specs) {
        this.userId = userId;
        this.name = name;
        this.specs = specs;
    }

    public SpecFile() {
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

    public List<SpecField> getSpecs() {
        return specs;
    }

    public void setSpecs(List<SpecField> specs) {
        this.specs = specs;
    }

    @Override
    public String toString() {
        return "SpecFile{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", specs=" + specs +
                '}';
    }
}
