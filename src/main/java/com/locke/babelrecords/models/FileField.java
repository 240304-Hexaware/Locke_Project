package com.locke.babelrecords.models;

public class FileField {
    private String name;
    private String data;

    private String dataType;

    public FileField(String name, String data, String dataType) {
        this.name = name;
        this.data = data;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "FileField{" +
                "name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
