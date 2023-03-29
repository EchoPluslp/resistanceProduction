package com.project.resistanceproduction.controller;

import com.project.resistanceproduction.Utils.RUtils;
import com.project.resistanceproduction.Utils.Renum;
import com.project.resistanceproduction.Utils.Result;
import com.project.resistanceproduction.entity.User;
import com.project.resistanceproduction.entity.UserLogin;
import com.project.resistanceproduction.service.impl.userServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/user")
public class LoginController {

    @Autowired
    userServiceImpl userService;

    @RequestMapping("/login")
    public Result login(UserLogin user) {
        User loginUser  = new User(user.getUserName(),user.getPassWord());
        User userContext  = userService.getUserByNameAndPassword(loginUser);
        if (userContext == null){
            return RUtils.Err(Renum.USER_NOT_EXIST.getCode(),Renum.USER_NOT_EXIST.getMsg());
        }
    return RUtils.success(userContext);
    }
}
