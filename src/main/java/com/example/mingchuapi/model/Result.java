package com.example.mingchuapi.model;

import java.io.Serializable;
import java.util.Date;

public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String resultCode;
    private String resultMsg;
    private T data;
    private Date time;

    public Result() {}
    public Result(CodeEnum codeenum, T data) {
        this.resultCode = codeenum.getCode();
        this.resultMsg = codeenum.getMsg();
        this.data = data;
        this.time = new Date();
    }

    public Result(String resultCode, String resultMsg, T data) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.data = data;
        this.time = new Date();
    }

    public String getResultCode() {return resultCode;}
    public void setResultCode(String resultCode) {this.resultCode = resultCode;}

    public String getResultMsg() {return resultMsg;}
    public void setResultMsg(String resultMsg) {this.resultMsg = resultMsg;}

    public T getData() {return data;}
    public void setData(T data) {this.data = data;}

    public Date getTime() {return time;}
    public void setTime(Date time) {this.time = time;}
}
