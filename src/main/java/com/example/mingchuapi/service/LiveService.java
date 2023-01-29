package com.example.mingchuapi.service;

import com.example.mingchuapi.model.LiveModel;
import org.springframework.stereotype.Service;

@Service
public class LiveService {

    public LiveModel getLiveUrl(String url, String deviceId, long timestamp) {
        LiveModel liveModel = new LiveModel();
        return  liveModel;
    }


}
