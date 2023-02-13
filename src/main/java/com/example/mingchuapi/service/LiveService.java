package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.config.Constants;
import com.example.mingchuapi.enums.AndmuCode;
import com.example.mingchuapi.model.CodeEnum;
import com.example.mingchuapi.util.AndmuUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class LiveService {

    @Value("${url.liveurl}")
    private String liveurl;

    @Value("${url.livebackurl}")
    private String  livebackurl;

    @Autowired
    private AndmuTokenService andmuTokenService;


    public Map<String,String> getLiveUrl(String deviceId,long timestamp) {
        Map<String,String> mapResult = new HashMap<>();
        if (StringUtils.isBlank(deviceId)) {
            mapResult.put(Constants.CODE_KEY,CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            mapResult.put(Constants.DETAIL_KEY,"缺失参数：device_id");
            return mapResult;
        }
        // 封装请求参数
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("deviceId", deviceId);
        String paramStr = JSONObject.toJSONString(param);

        String rsp = AndmuUtils.sendPost(liveurl, paramStr, andmuTokenService.getToken());

        // 处理请求结果
        JSONObject resultJson = JSON.parseObject(rsp);

        if (resultJson == null) {
            mapResult.put(Constants.CODE_KEY,CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            mapResult.put(Constants.DETAIL_KEY,"远程请求异常！");
        } else {
            String resultCode = resultJson.getString("resultCode");
            if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
                mapResult.put("url", resultJson.getJSONObject("data").getString("url"));
                mapResult.put("expires_in", resultJson.getJSONObject("data").getString("expiresIn"));
            } else {
                mapResult.put(Constants.CODE_KEY,CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
                mapResult.put(Constants.DETAIL_KEY,resultJson.getString("resultMsg"));
            }
        }
        return mapResult;
    }

    public Map<String,String> getLiveBackUrl(String deviceId, String start_time, String stop_time) {
        Map<String,String> mapResult = new HashMap<>();
        if (StringUtils.isBlank(deviceId)) {
            mapResult.put(Constants.CODE_KEY,CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            mapResult.put(Constants.DETAIL_KEY,"缺失参数：device_id");
            return mapResult;
        }

        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 封装请求参数
            SortedMap<String, Object> param = new TreeMap<>();
            param.put("deviceId", deviceId);
            param.put("startTime",format.parse(start_time).getTime());
            param.put("endTime",format.parse(stop_time).getTime());


        String paramStr = JSONObject.toJSONString(param);

        String rsp = AndmuUtils.sendPost( livebackurl, paramStr, andmuTokenService.getToken());

        // 处理请求结果
        JSONObject resultJson = JSON.parseObject(rsp);

        if (resultJson == null) {
            mapResult.put(Constants.CODE_KEY,CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            mapResult.put(Constants.DETAIL_KEY,"远程请求异常！");
        } else {
            String resultCode = resultJson.getString("resultCode");
            if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
                mapResult.put("url", resultJson.getJSONObject("data").getString("hlsUrl"));

            } else {
                mapResult.put(Constants.CODE_KEY,CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
                mapResult.put(Constants.DETAIL_KEY,resultJson.getString("resultMsg"));
            }
        }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return mapResult;
    }


}
