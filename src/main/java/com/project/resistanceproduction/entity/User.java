package com.project.resistanceproduction.entity;

import lombok.Data;

@Data
public class User {
    private String username;

    private String password;

    private String token;

    public User(String userName, String passWord) {
        this.username = userName;
        this.password = passWord;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}