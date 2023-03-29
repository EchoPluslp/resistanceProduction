package com.project.resistanceproduction.Utils;

public enum STATUS {
    ONLINE(1, "在线"),
    OFFLINE(0, "离线"),
    HITCH(-1, "故障"),
    ;
    private Integer code;
    private String message;


    STATUS(Integer code, String message) {
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
