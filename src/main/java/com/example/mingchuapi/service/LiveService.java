package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.config.Constants;
import com.example.mingchuapi.enums.AndmuCode;
import com.example.mingchuapi.model.CodeEnum;
import com.example.mingchuapi.util.AndmuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
@Slf4j
public class LiveService {

    @Value("${url.liveurl}")
    private String liveurl;

    @Value("${url.livebackurl}")
    private String livebackurl;

    @Autowired
    private AndmuTokenService andmuTokenService;
    @Autowired
    private DeviceService deviceService;

    /**
     * 获取视频直播链接
     *
     * @param deviceId
     * @param timestamp
     * @param response
     * @return
     */
    public Map<String, Object> getLiveUrl(String deviceId, long timestamp, HttpServletResponse response) {
        // 验证设备在线状态
        Map<String, Object> resultMap = deviceService.checkDeviceStatus(deviceId, response);
        if (!resultMap.isEmpty()) {
            return resultMap;
        }

        // 定义返回参数
        Map<String, Object> mapResult = new HashMap<>(16);

        // 封装请求参数
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("deviceId", deviceId);
        String paramStr = JSON.toJSONString(param);

        String rsp = AndmuUtils.sendPost(liveurl, paramStr, andmuTokenService.getToken());
        log.info("请求返回值rsp={}", rsp);

        // 处理请求结果
        JSONObject resultJson = JSON.parseObject(rsp);
        if (resultJson == null) {
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
            mapResult.put(Constants.DETAIL_KEY, "远程请求异常！");
            return mapResult;
        }

        // 结果状态码
        String resultCode = resultJson.getString(Constants.RESULT_CODE_KEY_CAMEL);

        // 处理成功返回的结果信息
        if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
            mapResult.clear();
            mapResult.put("url", resultJson.getJSONObject("data").getString("url"));
            mapResult.put("expires_in", resultJson.getJSONObject("data").getString("expiresIn"));
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_SUCCESS.getCode()));
            return mapResult;
        }

        // 处理返回代码不是 200 状态的信息
        resultMap.put(Constants.DETAIL_KEY, resultJson.getString(Constants.RESULT_MSG_CAMEL));
        return deviceService.dealNotSuccessStatus(resultCode, mapResult, response);

    }

    /**
     * 获取回放链接
     *
     * @param deviceId
     * @param start_time
     * @param stop_time
     * @param response
     * @return
     */
    public Map<String, Object> getLiveBackUrl(String deviceId, String start_time, String stop_time, HttpServletResponse response) {
        // 验证设备在线状态
        Map<String, Object> resultMap = deviceService.checkDeviceStatus(deviceId, response);
        if (!resultMap.isEmpty()) {
            return resultMap;
        }

        // 定义返回参数
        Map<String, Object> mapResult = new HashMap<>(16);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 封装请求参数
            SortedMap<String, Object> param = new TreeMap<>();
            param.put("deviceId", deviceId);
            param.put("startTime", format.parse(start_time).getTime());
            param.put("endTime", format.parse(stop_time).getTime());

            String paramStr = JSON.toJSONString(param);

            String rsp = AndmuUtils.sendPost(livebackurl, paramStr, andmuTokenService.getToken());
            log.info("请求返回值rsp={}", rsp);

            // 处理请求结果
            JSONObject resultJson = JSON.parseObject(rsp);
            if (resultJson == null) {
                response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
                mapResult.put(Constants.DETAIL_KEY, "远程请求异常！");
                return mapResult;
            }

            // 结果状态码
            String resultCode = resultJson.getString("resultCode");

            // 处理成功返回的结果信息
            if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
                mapResult.clear();
                mapResult.put("url", resultJson.getJSONObject("data").getString("hlsUrl"));
                response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_SUCCESS.getCode()));
                return mapResult;

            }
            // 处理返回代码不是 200 状态的信息
            resultMap.put(Constants.DETAIL_KEY, resultJson.getString(Constants.RESULT_MSG_CAMEL));
            return deviceService.dealNotSuccessStatus(resultCode, mapResult, response);

        } catch (ParseException e) {
            mapResult.put(Constants.DETAIL_KEY, "时间格式化异常");
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
        }
        return mapResult;
    }
}
