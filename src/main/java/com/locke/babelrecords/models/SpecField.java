package com.locke.babelrecords.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class SpecField {
  private String name;
  private int start;

  private int end;

  private String dataType;

  public SpecField() {
  }

  public SpecField(String name, int start, String dataType) {
    this.name = name;
    this.start = start;
    this.dataType = dataType;
  }

  public String getName() {
    return name;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public String getDataType() {
    return dataType;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  @Override
  public String toString() {
    return "SpecField{" +
        "name='" + name + '\'' +
        ", start=" + start +
        ", dataType='" + dataType + '\'' +
        '}';
  }
}
