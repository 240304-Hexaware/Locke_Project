package com.locke.babelrecords.models;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;

@Document("roles")
public class Role implements GrantedAuthority {

  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field
  private String authority;

  public Role() {
  }

  public Role(String authority) {
    this.authority = authority;
  }

  public Role(String roleId, String authority) {
    this.id = roleId;
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
