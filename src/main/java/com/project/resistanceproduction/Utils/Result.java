package com.project.resistanceproduction.Utils;

import lombok.Data;

@Data
public class Result <T>{
    /*返回体*/
    public  Integer code;
    public String msg;
    public T data;

}
