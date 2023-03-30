package com.project.resistanceproduction.Utils;

public enum NGOK {
    OK(1, "良品"),
    NG(0, "次品"),
    ;
    private Integer code;
    private String message;

    NGOK(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }
}
