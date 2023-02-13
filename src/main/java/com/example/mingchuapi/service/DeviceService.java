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
	public Map getDeviceStatus(String shopId, String deviceId, HttpServletResponse response) {
		return this.getDeviceStatus(new HashMap<>(), deviceId, response);
	}

	private Map getDeviceStatus(HashMap<String, Object> resultMap, String deviceId, HttpServletResponse response) {

		if (StringUtils.isBlank(deviceId)) {
			response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
			resultMap.put(Constants.DETAIL_KEY, "缺失参数：device_id");
			return resultMap;
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
			resultMap.put(Constants.DETAIL_KEY, "远程请求异常！");
			response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
		} else {
			String resultCode = resultJson.getString("resultCode");
			resultMap.put(Constants.DETAIL_KEY, resultJson.getString("resultMsg"));

			if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
				// 请求成功
				JSONArray data = resultJson.getJSONArray("data");

				resultMap.clear();
				int resStatus = data.getJSONObject(0).getIntValue("deviceStatus");
				// 接口返回值转换（云眼离线状态为0，名厨离线状态为2）
				if (0 == resStatus) {
					resStatus = 2;
				}
				resultMap.put("device_status", resStatus);
				return resultMap;
			} else if (StringUtils.equalsAny(resultCode, AndmuCode.TOKEN_EXPIRATION_ERROR.getEcode(), AndmuCode.TOKEN_INVALID_ERROR.getEcode())) {
				// token异常
				response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL.getCode()));
			} else {
				// 其他
				response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
			}
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
	public Map getScreenshot(String shopId, String deviceId, String channelIdx, HttpServletResponse response) {
		return this.getRealtimeThumbnail(new HashMap<>(), deviceId, response);
	}

	private Map getRealtimeThumbnail(HashMap<String, Object> resultMap, String deviceId, HttpServletResponse response) {
		if (StringUtils.isBlank(deviceId)) {
			response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));

			resultMap.put(Constants.DETAIL_KEY, "缺失参数：device_id");
			return resultMap;
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
			response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
			resultMap.put(Constants.DETAIL_KEY, "远程请求异常！");
		} else {
			String resultCode = resultJson.getString("resultCode");
			resultMap.put(Constants.DETAIL_KEY, resultJson.getString("resultMsg"));

			if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
				// 请求成功
				resultMap.clear();
				resultMap.put("url", resultJson.getJSONObject("data").getString("url"));
				return resultMap;
			} else if (StringUtils.equalsAny(resultCode, AndmuCode.TOKEN_EXPIRATION_ERROR.getEcode(), AndmuCode.TOKEN_INVALID_ERROR.getEcode())) {
				// token 异常
				response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL.getCode()));
			} else {
				// 其他
				response.setStatus(Integer.parseInt(CodeEnum.RESULT_CODE_FAIL_OTHER.getCode()));
			}
		}
		return resultMap;
	}

}
