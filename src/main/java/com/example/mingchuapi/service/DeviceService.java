package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.config.Constants;
import com.example.mingchuapi.enums.AndmuCode;
import com.example.mingchuapi.model.CodeEnum;
import com.example.mingchuapi.util.AndmuUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author: zhangwentao
 * @CreateTime: 2023/1/31
 * @Description: 设备相关服务
 * @Version: 1.0
 */
@Service
@Slf4j
public class DeviceService {
    @Autowired
    private AndmuTokenService andmuTokenService;

    @Value("${url.realtimeThumbnailUrl}")
    private String realtimeThumbnailUrl;
    @Value("${url.deviceInfoUrl}")
    private String deviceInfoUrl;


    /**
     * 平台监测设备健康状态。
     *
     * @param shopId   店铺唯一 ID
     * @param deviceId 设备 ID 必填
     * @param response
     * @return Result
     * 设备在线状态
     * 1：在线；2：离线
     */
    public Map<String, Object> getDeviceStatus(String shopId, String deviceId, HttpServletResponse response) {
        return this.getDeviceStatus(new HashMap<>(), deviceId, response);
    }

    /**
     * 远程查询设备状态
     *
     * @param resultMap
     * @param deviceId
     * @param response
     * @return
     */
    private Map<String, Object> getDeviceStatus(Map<String, Object> resultMap, String deviceId, HttpServletResponse response) {
        if (!this.checkParam(deviceId, resultMap, response).isEmpty()) {
            return resultMap;
        }

        // 封装请求参数
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("deviceId", deviceId);
        String paramStr = JSON.toJSONString(param);

        log.info("device status param ->" + paramStr);
        String rsp = AndmuUtils.sendPost(deviceInfoUrl, paramStr, andmuTokenService.getToken());
        log.info("device status response result ->" + rsp);

        // 处理请求结果
        JSONObject resultJson = JSON.parseObject(rsp);
        if (resultJson == null) {
            resultMap.put(Constants.DETAIL_KEY, "远程请求异常！");
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
            return resultMap;
        }

        // 结果状态码
        String resultCode = resultJson.getString(Constants.RESULT_CODE_KEY_CAMEL);
        // 处理成功返回的结果信息
        if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
            JSONArray data = resultJson.getJSONArray("data");
            int resStatus = data.getJSONObject(0).getIntValue(Constants.DEVICE_STATUS_KEY_CAMEL);
            // 接口返回值转换（云眼离线状态为0，名厨离线状态为2）
            if (Constants.NUM_ZERO == resStatus) {
                resStatus = Constants.NUM_TWO;
            }

            resultMap.clear();
            resultMap.put(Constants.DEVICE_STATUS_KEY, resStatus);
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_SUCCESS.getCode()));
            return resultMap;
        }

        // 处理返回代码不是 200 状态的信息
        resultMap.put(Constants.DETAIL_KEY, resultJson.getString(Constants.RESULT_MSG_CAMEL));
        return this.dealNotSuccessStatus(resultCode, resultMap, response);
    }

    /**
     * @description: 处理不成功的结果返回状态码
     * @author: zhangwentao
     * @date: 2023/2/14 上午9:25
     * @param: [resultCode, resultMap, response]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    public Map<String, Object> dealNotSuccessStatus(String resultCode, Map<String, Object> resultMap, HttpServletResponse response) {
        if (StringUtils.equalsAny(resultCode, AndmuCode.TOKEN_EXPIRATION_ERROR.getEcode(), AndmuCode.TOKEN_INVALID_ERROR.getEcode())) {
            // token异常
            log.error("--------------token验证异常，重新获取----------");
            andmuTokenService.createToken();
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL.getCode()));
        } else {
            // 其他
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
        }
        return resultMap;
    }

    /**
     * @description: 参数验证
     * @author: zhangwentao
     * @date: 2023/2/14 上午9:15
     * @param: [deviceId, resultMap, response]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    public Map<String, Object> checkParam(String deviceId, Map<String, Object> resultMap, HttpServletResponse response) {
        if (StringUtils.isBlank(deviceId)) {
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
            resultMap.put(Constants.DETAIL_KEY, "缺失参数：device_id");
        }
        return resultMap;
    }

    /**
     * @param shopId     店铺唯一 ID
     * @param deviceId   设备 ID
     * @param channelIdx 通道号
     * @param response
     * @description: 截取指定设备图片
     * @author: zhangwentao
     * @date: 2023/1/31 下午1:48
     * @param: [shopId, deviceId, channelIdx]
     * @return: com.example.mingchuapi.model.Result
     **/
    public Map<String, Object> getScreenshot(String shopId, String deviceId, String channelIdx, HttpServletResponse response) {
        // 验证设备在线状态
        Map<String, Object> resultMap = this.checkDeviceStatus(deviceId, response);
        if (!resultMap.isEmpty()) {
            return resultMap;
        }
        return this.getRealtimeThumbnail(new HashMap<>(), deviceId, response);
    }

    /**
     * @description: 验证当前设备是否在线
     * @author: zhangwentao
     * @date: 2023/2/14 上午10:05
     * @param: [deviceId, response]
     * @return: java.util.HashMap<java.lang.String, java.lang.Object>
     **/
    public Map<String, Object> checkDeviceStatus(String deviceId, HttpServletResponse response) {
        Map<String, Object> hashMap = new HashMap<>();
        this.getDeviceStatus(hashMap, deviceId, response);
        String statusStr = String.valueOf(response.getStatus());
        // 非成功状态直接返回
        if (!StringUtils.equals(CodeEnum.RESULT_CODE_SUCCESS.getCode(), statusStr)) {
            return hashMap;
        }
        // 离线状态
        if (Constants.NUM_TWO == Integer.parseInt(hashMap.get(Constants.DEVICE_STATUS_KEY).toString())) {
            hashMap.put(Constants.DETAIL_KEY, "设备已离线");
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
            return hashMap;
        }
        return new HashMap<>(16);
    }

    /**
     * 远程获取实时截图
     *
     * @param resultMap
     * @param deviceId
     * @param response
     * @return
     */
    private Map<String, Object> getRealtimeThumbnail(HashMap<String, Object> resultMap, String deviceId, HttpServletResponse response) {
        if (!this.checkParam(deviceId, resultMap, response).isEmpty()) {
            return resultMap;
        }

        // 封装参数
        SortedMap<String, Object> param = new TreeMap<>();
        param.put("deviceId", deviceId);
        String paramStr = JSON.toJSONString(param);
        log.info("realtime thumbnail param ->" + paramStr);

        String rsp = AndmuUtils.sendPost(realtimeThumbnailUrl, paramStr, andmuTokenService.getToken());
        log.info("realtime thumbnail result ->" + rsp);

        // 处理请求结果
        JSONObject resultJson = JSON.parseObject(rsp);
        if (resultJson == null) {
            response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
            resultMap.put(Constants.DETAIL_KEY, "远程请求异常！");
            return resultMap;
        }

        // 结果状态码
        String resultCode = resultJson.getString(Constants.RESULT_CODE_KEY_CAMEL);
        // 处理成功返回的结果信息
        if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
            // 请求成功
            resultMap.clear();
            resultMap.put("url", resultJson.getJSONObject("data").getString("url"));
            return resultMap;
        }

        // 处理返回代码不是 200 状态的信息
        resultMap.put(Constants.DETAIL_KEY, resultJson.getString(Constants.RESULT_MSG_CAMEL));
        return this.dealNotSuccessStatus(resultCode, resultMap, response);
    }

}
