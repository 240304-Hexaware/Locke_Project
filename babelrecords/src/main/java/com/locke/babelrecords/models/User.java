package com.locke.babelrecords.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document("users")
public class User implements UserDetails {
  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field(name = "username")
  private String username;

  @Field(name = "password")
  private String password;

  @Field(name = "role")
  private List<Role> roles;

  @Field(name = "createdAt")
  @CreatedDate
  private Date createdAt;

  @Field
  private List<String> specFileIds;

  @Field
  private List<String> parsedFileIds;

  @Field
  private List<String> recordIds;


  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.roles = new ArrayList<>();
    this.roles.add(new Role("USER"));
    this.specFileIds = new ArrayList<>();
    this.parsedFileIds = new ArrayList<>();
    this.recordIds = new ArrayList<>();
  }

  public User(String username, String password, List<Role> role) {
    this.username = username;
    this.password = password;
    this.roles = role;
    this.specFileIds = new ArrayList<>();
    this.parsedFileIds = new ArrayList<>();
    this.recordIds = new ArrayList<>();
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @JsonProperty
  public void setPassword(String password) {
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  public List<Role> getRole() {
    return roles;
  }

  public void addRole(Role role) {
    this.roles.add(role);
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public List<String> getSpecFileIds() {
    return specFileIds;
  }

  public void addSpecFile(String specFile) {
    this.specFileIds.add(specFile);
  }

  public List<String> getParsedFileIds() {
    return parsedFileIds;
  }

  public void addParsedFile(String parsedFile) {
    this.parsedFileIds.add(parsedFile);
  }

  public List<String> getRecordIds() {
    return recordIds;
  }

  public void addRecords(List<String> records) {
    this.recordIds.addAll(records);
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", roles=" + roles +
        ", createdAt=" + createdAt +
        ", specFileIds=" + specFileIds +
        ", parsedFileIds=" + parsedFileIds +
        ", recordIds=" + recordIds +
        '}';
  }
}
