package com.project.resistanceproduction.mapper;

import com.project.resistanceproduction.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User getUserByNameAndPassword(@Param("user") User user);
}