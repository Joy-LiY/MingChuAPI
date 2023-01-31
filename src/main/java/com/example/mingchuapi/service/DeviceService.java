package com.example.mingchuapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.enums.AndmuCode;
import com.example.mingchuapi.model.AndmuResult;
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
	private String realtime_thumbnail_url;
	@Value("${url.deviceInfoUrl}")
	private String device_info_url;

	/**
	 * 平台监测设备健康状态。
	 *
	 * @param shop_id   店铺唯一 ID
	 * @param device_id 设备 ID 必填
	 * @return Result
	 * 设备在线状态
	 * 1：在线；2：离线
	 */
	public Result getDeviceStatus(String shop_id, String device_id) {
		Result result = new Result();
		getDeviceStatus(result, device_id);
		return result;
	}

	public Result getDeviceStatus(Result result, String deviceId) {

		if (StringUtils.isBlank(deviceId)) {
			result.setCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
			result.setMsg("缺失参数：device_id");
			return result;
		}
		// 封装请求参数
		SortedMap<String, Object> param = new TreeMap<>();
		param.put("deviceId", deviceId);
		String paramStr = JSONObject.toJSONString(param);

		log.info("device status param ->" + paramStr);
		String rsp = AndmuUtils.sendPost(device_info_url, paramStr, andmuTokenService.getToken());
		log.info("device status result ->" + rsp);

		// 处理请求结果
		JSONObject resultJson = JSON.parseObject(rsp);
		if (resultJson == null) {
			result.setCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
			result.setMsg("远程请求异常！");
		} else {
			String resultCode = resultJson.getString("resultCode");
			if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
				result.setCode(CodeEnum.RESULT_CODE_SUCCESS.getCode());
				result.setMsg(CodeEnum.RESULT_CODE_SUCCESS.getMsg());

				JSONArray data = resultJson.getJSONArray("data");
				HashMap<String, Integer> map = new HashMap<>();
				map.put("device_status", data.getJSONObject(0).getIntValue("deviceStatus"));

				result.setData(map);
			} else {
				result.setCode(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode());
				result.setMsg(resultJson.getString("resultMsg"));
			}
		}
		return result;
	}


	public AndmuResult getRealtimeThumbnail(String deviceId) {
		AndmuResult result = new AndmuResult();
		SortedMap<String, Object> param = new TreeMap<>();
		param.put("deviceId", deviceId);
		String paramStr = JSONObject.toJSONString(param);
		log.info("realtime thumbnail param ->" + paramStr);
		String rsp = AndmuUtils.sendPost(realtime_thumbnail_url, paramStr, andmuTokenService.getToken());
		log.info("realtime thumbnail result ->" + rsp);
		JSONObject resultJson = JSONObject.parseObject(rsp);
		if (resultJson == null) {
			result.setCode(AndmuCode.SERVER_INTERNAL_ERROR.getEcode());
		} else {
			String resultCode = resultJson.getString("resultCode");
			result.setCode(resultCode);
			if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
				String url = resultJson.getJSONObject("data").getString("url");
				result.setResult(url);
			}
		}
		return result;
	}


}
