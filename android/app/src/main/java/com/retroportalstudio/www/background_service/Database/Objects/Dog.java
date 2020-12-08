package com.retroportalstudio.www.background_service.Database.Objects;

// "CREATE TABLE IF NOT EXISTS dogs(id INTEGER PRIMARY KEY, name TEXT, age INTEGER)",

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "dogs")
public class Dog {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    int age;

    @Ignore
    public Dog(String name,int age) {
        this.name = name;
        this.age = age;
    }

    public Dog(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}