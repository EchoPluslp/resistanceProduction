package com.project.resistanceproduction.Utils;

public class RUtils {
    /*成功，且返回体有数据*/
    public static Result success(Object object) {
        Result r = new Result();
        r.setCode(Renum.SUCCESS.getCode());
        r.setMsg(Renum.SUCCESS.getMsg());
        r.setData(object);
        return r;
    }
    //成功，但返回体没数据
    public static  Result success(){

        return success(null);
    }
    //失败返回信息
    public static Result Err(Integer code,String msg){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}

