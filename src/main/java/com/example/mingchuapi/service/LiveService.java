package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.model.LiveModel;
import com.example.mingchuapi.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiveService {

    HttpUtils httpUtils;

    public LiveModel getLiveUrl(String url, String deviceId, long timestamp) {
        LiveModel liveModel = new LiveModel();
        liveModel.setDeviceId(deviceId);
        if (timestamp != 0) {
            liveModel.setEndTime(timestamp);
        }
        String liveurl = httpUtils.sendPost(url, JSONObject.toJSONString(liveModel));
        return  liveModel;
    }


}
