package com.project.resistanceproduction.service.impl;

import com.project.resistanceproduction.entity.User;
import com.project.resistanceproduction.mapper.UserMapper;
import com.project.resistanceproduction.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private UserMapper userMap;


    @Override
    public User getUserByNameAndPassword(User user){

        return userMap.getUserByNameAndPassword(user);
    }

}
