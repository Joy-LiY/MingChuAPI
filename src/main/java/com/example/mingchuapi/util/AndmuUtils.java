package com.example.mingchuapi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mingchuapi.enums.AndmuCode;
import com.example.mingchuapi.model.AndmuResult;
import com.example.mingchuapi.util.sign.Base64;
import com.example.mingchuapi.util.sign.Md5Utils;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class AndmuUtils {
    private static final Logger log = LoggerFactory.getLogger(AndmuUtils.class);

    private static final String TOKEN_URL = "https://open.andmu.cn/v3/open/api/token";

    private static final String RSA_PRIVATE_KEY = "MIICcwIBADANBgkqhkiG9w0BAQEFAASCAl0wggJZAgEAAoGBANV8eDQvG7rKYf5ww7PTZ4ikgjxFbiu590NLjE9lzfxC2KmAs8Tod93fMgjg8BJIttRKn6/rrMVsSK2FJT7fl/HjXsvKJcy5H9Wm/ZsFB+pI30UU7LzQkrieTmGf5q2TsRtn8gG6qU/Yb8t/ykogehlTz5iaz/LW+QFN4fogz6u5AgMBAAECfzs1SiEu74Hen07x91ToTM6Y0YXlu6hk6y6+xStAEODlqTFZgIIb9yKRxE6yE+L+R7aY+7DOrBwU7BMz2iCV4o2xf2MAW388JznmjjvT+gjYxBJjtt1UjlESLkPvps0Xo6gvAFF3+K1yeDyA4HPAtt7l2+FTylCElTbSadxOuj0CQQD7L1IwVvqVZtTccBrU17L1DzwodG1BJSdysvXEUsetXnkzXIsXFC/YNg/Jl7bJkFSFtDrbreUigd6FCi/d89HrAkEA2ZQk3zzMZeKbo613l/47kdPvH7hLyzUmWK/XmvnNlWkr26zkuCwI26NwFyRQKwuTuXKznEPLsTrOZTpJdtKr6wJASkgrQRpnzojziCEq/iivxqLzwm7z5GENcnEUFzP30wBuTU8f3vpcT89lCdPw/VjOdh/fjBm3+mV3ndZuY2/4cwJAFjeOXE01AeJVyizYXWjUZFujuzVyZ9mRhNTPyz9ewrbuh4vr/vJgJS1XMMXAeeTAvNr9hCNYFUzP7n45Kizw6QJAZK0JZ7uIj+MGHegyM7yQsn+f/RIGbZO1J0w9D+zn2B2sjwE9MdDfAAG3WrGi2m18Bj/nyppv86zkaa8hiZ8m0Q==";

    private static final String APP_ID = "6e944bad09b248798cff78504b3b609c";

    private static final String APP_SECRET = "D0zPQqyxtCKkcykC";

    private static final String VERSION = "1.0.0";

    public static AndmuResult getToken() {
        AndmuResult result = new AndmuResult();
        try {
            String sig = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(StringUtils.getBytes(APP_ID + APP_SECRET, Charsets.toCharset("UTF-8"))));
            SortedMap<String, Object> param = new TreeMap<>();
            param.put("operatorType", 1);
            param.put("sig", sig);
            String paramStr = JSON.toJSONString(param);


            log.info("get token param ->" + paramStr);
            String rsp = sendPost(TOKEN_URL, paramStr, null);
            log.info("get token result ->" + rsp);


            JSONObject resultJson = JSON.parseObject(rsp);
            String resultCode = resultJson.getString("resultCode");
            result.setCode(resultCode);
            if (AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
                String token = resultJson.getJSONObject("data").getString("token");
                result.setResult(token);
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("get token error...", e);
        }

        return result;
    }

    //	public static String getDeviceList(String token, int page, int pageSize) {
//		SortedMap<String,Object> param = new TreeMap<>();
//		param.put("page", page);
//		param.put("pageSize", pageSize);
//		String paramStr = JSONObject.toJSONString(param);
//		log.info("device list param ->" + paramStr);
//		String result = sendPost(DEVICE_LIST_URL, paramStr, token);
//		log.info("device list result ->" + result);
//		JSONObject resultJson = JSONObject.parseObject(result);
//		String resultCode = resultJson.getString("resultCode");
//		if(AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
//
//		}
//		return result;
//	}
//
//	public static AndmuResult getWedsdkPlayer(String token, String deviceId) {
//		AndmuResult result = new AndmuResult();
//		SortedMap<String,Object> param = new TreeMap<>();
//		param.put("deviceId", deviceId);
//		String paramStr = JSONObject.toJSONString(param);
//		log.info("websdk player param ->" + paramStr);
//		String rsp = sendPost(WEBSDK_PLAYER_URL, paramStr, token);
//		log.info("websdk player result ->" + rsp);
//		JSONObject resultJson = JSONObject.parseObject(rsp);
//		if(resultJson == null) {
//			result.setCode(AndmuCode.SERVER_INTERNAL_ERROR.getEcode());
//		}else {
//			String resultCode = resultJson.getString("resultCode");
//			result.setCode(resultCode);
//			if(AndmuCode.SUCCESS.getEcode().equals(resultCode)) {
//				String player = resultJson.getJSONObject("data").getString("url");
//				result.setResult(player);
//			}
//		}
//		return result;
//	}
//

    private static String signature(String md5, String timestamp, String token) {
        SortedMap<String, String> signMap = new TreeMap<>();
        signMap.put("appid", APP_ID);
        signMap.put("md5", md5);
        signMap.put("timestamp", timestamp);
        if (StringUtils.isNotEmpty(token)) {
            signMap.put("token", token);
        }
        signMap.put("version", VERSION);
        String text = JSONObject.toJSONString(signMap);
        System.out.println(text);
        String signatureStr = null;
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(RSA_PRIVATE_KEY));
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(text.getBytes("utf-8"));
            signatureStr = Base64.encode(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signatureStr;
    }

    /**
     * post 请求
     *
     * @param url   请求地址
     * @param param 请求参数
     * @param token tocken值
     * @return
     */
    public static String sendPost(String url, String param, String token) {
        Map<String, String> headers = new HashMap<>();
        String md5 = Md5Utils.bit32(param).toLowerCase();
        headers.put("appid", APP_ID);
        headers.put("md5", md5);
        headers.put("version", VERSION);
        String timestamp = String.valueOf(System.currentTimeMillis());
        headers.put("timestamp", timestamp);
        if (StringUtils.isNotEmpty(token)) {
            headers.put("token", token);
        }
        headers.put("signature", signature(md5, timestamp, token));
        return HttpUtils.sendPostWithJson(url, param, headers);
    }

    /**
     * 测试类
     *
     * @param args
     */
    public static void main(String[] args) {
        AndmuResult andmuResult = getToken();
        log.info("请求结果为：{}", andmuResult.toString());
    }
}
