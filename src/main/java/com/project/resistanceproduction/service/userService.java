package com.project.resistanceproduction.service;

import com.project.resistanceproduction.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

public interface userService {

    User getUserByNameAndPassword(@Param("user") User user);
}
