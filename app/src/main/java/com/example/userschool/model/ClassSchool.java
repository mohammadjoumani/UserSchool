package com.example.userschool.model;public class ClassSchool {    private int id;    private String name;    private int countStudent;    public ClassSchool(int id, String name, int countStudent) {        this.id = id;        this.name = name;        this.countStudent = countStudent;    }    public ClassSchool(int id, String name) {        this.id = id;        this.name = name;    }    public ClassSchool() {    }    public int getCountStudent() {        return countStudent;    }    public void setCountStudent(int countStudent) {        this.countStudent = countStudent;    }    public int getId() {        return id;    }    public void setId(int id) {        this.id = id;    }    public String getName() {        return name;    }    public void setName(String name) {        this.name = name;    }}