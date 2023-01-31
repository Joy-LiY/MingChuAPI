package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.enums.AndmuCode;
import com.example.mingchuapi.model.CodeEnum;
import com.example.mingchuapi.model.Result;
import com.example.mingchuapi.util.AndmuUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
	 * @return Result
	 * 设备在线状态
	 * 1：在线；2：离线
	 */
	public Result getDeviceStatus(String shopId, String deviceId) {
		return this.getDeviceStatus(new Result(), deviceId);
	}

	private Result getDeviceStatus(Result result, String deviceId) {

		if (StringUtils.isBlank(deviceId)) {
			result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
			result.setResultMsg("缺失参数：device_id");
			return result;
		}
		// 封装请求参数
		SortedMap<String, Object> param = new TreeMap<>();
		param.put("deviceId", deviceId);
		String paramStr = JSONObject.toJSONString(param);

		log.info("device status param ->" + paramStr);
		String rsp = AndmuUtils.sendPost(deviceInfoUrl, paramStr, andmuTokenService.getToken());
		log.info("device status result ->" + rsp);

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

				JSONArray data = resultJson.getJSONArray("data");
				HashMap<String, Integer> map = new HashMap<>();
				map.put("device_status", data.getJSONObject(0).getIntValue("deviceStatus"));

				result.setData(map);
			} else {
				result.setResultCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
				result.setResultMsg(resultJson.getString("resultMsg"));
			}
		}
		return result;
	}

	/**
	 * @param channelIdx 通道号
	 * @param deviceId   设备 ID
	 * @param shopId     店铺唯一 ID
	 * @description: 截取指定设备图片
	 * @author: zhangwentao
	 * @date: 2023/1/31 下午1:48
	 * @param: [shopId, deviceId, channelIdx]
	 * @return: com.example.mingchuapi.model.Result
	 **/
	public Result getScreenshot(String shopId, String deviceId, String channelIdx) {
		return this.getRealtimeThumbnail(new Result(), deviceId);
	}

	private Result getRealtimeThumbnail(Result result, String deviceId) {
		if (StringUtils.isBlank(deviceId)) {
			result.setCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
			result.setMsg("缺失参数：device_id");
			return result;
		}

		// 封装参数
		SortedMap<String, Object> param = new TreeMap<>();
		param.put("deviceId", deviceId);
		String paramStr = JSONObject.toJSONString(param);
		log.info("realtime thumbnail param ->" + paramStr);

		String rsp = AndmuUtils.sendPost(realtimeThumbnailUrl, paramStr, andmuTokenService.getToken());
		log.info("realtime thumbnail result ->" + rsp);

		// 处理请求结果
		JSONObject resultJson = JSONObject.parseObject(rsp);
		if (resultJson == null) {
			result.setCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
			result.setMsg("远程请求异常！");
		} else {
			String resultCode = resultJson.getString("resultCode");
			result.setCode(resultCode);
			if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
				HashMap<String, String> map = new HashMap<>();
				map.put("url", resultJson.getJSONObject("data").getString("url"));
				result.setData(map);
			} else {
				result.setCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
				result.setMsg(resultJson.getString("resultMsg"));
			}
		}
		return result;
	}



}
