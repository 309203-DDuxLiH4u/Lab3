package com.example.customadapterdemo;

public class User {
    public String name;
    public String hometown;

    public User(String name, String hometown) {
        this.name = name;
        this.hometown=hometown;
    }
    public String getName() {
        return name;
    }
    public String getCity() {
        return hometown;
    }
}
