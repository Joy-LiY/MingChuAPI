package com.example.mingchuapi.model;

import lombok.Data;

@Data
public class LiveModel {

    //设备ID
    public String deviceId;

    //失效时间 大于当前时间的13位毫秒时间戳
    public long endTime;

    //B01接口 结果返回模型
    public String url;

    public String expires_in;
    
    public String heartbeat_time;

}
