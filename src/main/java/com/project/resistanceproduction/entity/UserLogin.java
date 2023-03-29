package com.project.resistanceproduction.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLogin implements Serializable {
    String userName;
    String passWord;
}
