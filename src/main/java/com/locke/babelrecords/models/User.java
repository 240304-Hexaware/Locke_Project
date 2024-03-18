package com.locke.babelrecords.models;

import com.locke.babelrecords.exceptions.InvalidRoleException;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("users")
public class User {
  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field(name = "username")
  private String username;

  @Field(name = "password")
  private String password;

  @Field(name = "loginToken")
  private String loginToken;

  @Field(name = "role")
  private String role;

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(String id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) throws InvalidRoleException {
    if ( !role.equalsIgnoreCase("admin") && !role.equalsIgnoreCase("user") ) {
      throw new InvalidRoleException();
    }

    this.role = role.toLowerCase();
  }
}
