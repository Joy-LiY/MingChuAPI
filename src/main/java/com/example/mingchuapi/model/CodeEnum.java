package com.example.mingchuapi.model;

/**
 * 全局返回码
 * @author ljx
 */
public enum CodeEnum {

    RESULT_CODE_SUCCESS("200", "SUCCESS"),
    RESULT_CODE_FAIL("401", "TOKEN校验失败"),
    RESULT_CODE_FAIL_OTHER("407","其他情况"),
    ;

    private String code;
    private String msg;
    CodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {return code;}
    public void setCode(String code) {this.code = code;}

    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}
}
