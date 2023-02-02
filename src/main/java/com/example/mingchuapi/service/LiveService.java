package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.enums.AndmuCode;
import com.example.mingchuapi.model.CodeEnum;
import com.example.mingchuapi.model.LiveModel;
import com.example.mingchuapi.model.Result;
import com.example.mingchuapi.model.UrlModel;
import com.example.mingchuapi.util.AndmuUtils;
import com.example.mingchuapi.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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


    public Result getLiveUrl(String deviceId,long timestamp) {
        Result result = new Result();
        if (StringUtils.isBlank(deviceId)) {
            result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            result.setResultMsg("缺失参数：device_id");
            return result;
        }
        // 封装请求参数
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("deviceId", deviceId);
        String paramStr = JSONObject.toJSONString(param);

        String rsp = AndmuUtils.sendPost(liveurl, paramStr, andmuTokenService.getToken());

        // 处理请求结果
        JSONObject resultJson = JSON.parseObject(rsp);

        if (resultJson == null) {
            result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            result.setResultMsg("远程请求异常！");
        } else {
            String resultCode = resultJson.getString("resultCode");
            if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
                result.setResultCode(CodeEnum.RESULT_CODE_SUCCESS.getCode());
                result.setResultMsg(CodeEnum.RESULT_CODE_SUCCESS.getMsg());

                HashMap<String, String> map = new HashMap<>();
                map.put("url", resultJson.getJSONObject("data").getString("url"));
                map.put("expiresIn",resultJson.getJSONObject("data").getString("expiresln"));

                result.setData(map);
            } else {
                result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
                result.setResultMsg(resultJson.getString("resultMsg"));
            }
        }
        return result;
    }

    public Result getLiveBackUrl(String deviceId, Date start_time, Date stop_time) {
        Result result = new Result();
        if (StringUtils.isBlank(deviceId)) {
            result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            result.setResultMsg("缺失参数：device_id");
            return result;
        }
        // 封装请求参数
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("deviceId", deviceId);
        param.put("start_time",start_time);
        param.put("stop_time",stop_time);
        String paramStr = JSONObject.toJSONString(param);

        String rsp = AndmuUtils.sendPost( livebackurl, paramStr, andmuTokenService.getToken());

        // 处理请求结果
        JSONObject resultJson = JSON.parseObject(rsp);

        if (resultJson == null) {
            result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
            result.setResultMsg("远程请求异常！");
        } else {
            String resultCode = resultJson.getString("resultCode");
            if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
                result.setResultCode(CodeEnum.RESULT_CODE_SUCCESS.getCode());
                result.setResultMsg(CodeEnum.RESULT_CODE_SUCCESS.getMsg());

                HashMap<String, String> map = new HashMap<>();
                map.put("url", resultJson.getJSONObject("data").getString("url"));

                result.setData(map);
            } else {
                result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
                result.setResultMsg(resultJson.getString("resultMsg"));
            }
        }
        return result;
    }


}
