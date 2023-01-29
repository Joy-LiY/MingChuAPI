package com.example.mingchuapi.model;

import java.io.Serializable;
import java.util.Date;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String msg;
    private T data;
    private Date time;

    public Result() {}
    public Result(CodeEnum codeenum, T data) {
        this.code = codeenum.getCode();
        this.msg = codeenum.getMsg();
        this.data = data;
        this.time = new Date();
    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.time = new Date();
    }

    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}

    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}

    public T getData() {return data;}
    public void setData(T data) {this.data = data;}

    public Date getTime() {return time;}
    public void setTime(Date time) {this.time = time;}
}
