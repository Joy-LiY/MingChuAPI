package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.model.LiveModel;
import com.example.mingchuapi.model.Result;
import com.example.mingchuapi.model.UrlModel;
import com.example.mingchuapi.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiveService {



    public LiveModel getLiveUrl(String url, String deviceId, long timestamp) {
        LiveModel liveModel = new LiveModel();
        liveModel.setDeviceId(deviceId);
        if (timestamp != 0) {
            liveModel.setEndTime(timestamp);
        }
        String liveurl = HttpUtils.sendPost(url, JSONObject.toJSONString(liveModel));
        Result result = (Result) JSONObject.parse(liveurl);
        if(!result.getResultCode().equals("000000")) {
            return new LiveModel();
        }
        UrlModel urlModel = (UrlModel) result.getData();
        liveModel.setUrl(urlModel.getUrl());
        return  liveModel;
    }


}
