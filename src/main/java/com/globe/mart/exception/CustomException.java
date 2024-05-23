package com.globe.mart.exception;

public class CustomException extends Exception {
    private String code;
    private String msg;

    public CustomException(String code,String msg) {
        setCode(code);
        setMsg(msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
