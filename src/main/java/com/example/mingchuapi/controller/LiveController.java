package com.example.mingchuapi.controller;

import com.example.mingchuapi.model.LiveModel;
import com.example.mingchuapi.model.Result;
import com.example.mingchuapi.service.LiveService;
import com.example.mingchuapi.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 直播相关接口
 * @author ljx
 */
@RestController
public class LiveController {

    HttpUtils httpUtils;

    LiveService liveService;

    @Value("${url.liveurl}")
    private String liveurl;


    /**
     *
     * @param shop_id 店铺唯一ID （食品经营许可证编码：JY + 14 位阿拉伯数字或CY + 13 位阿拉伯数字）
     * @param device_id 设备 ID
     * @param channel_idx 通道号
     * @param upload_rate 上传码率 （码流类型，0：高清；1：标清，默认 0）
     * @param live_mode 直播类型 （0：hls；1：flv；2：rtmp；默认：0）
     * @param enable_audio 启用音频 （0：否；1：是；默认：0）
     * @param expire_time 过期时长 （单位：秒；默认为 24 * 60 * 60 秒）
     * @return
     */
    @RequestMapping(value = "/live-address", method = RequestMethod.GET)
    @ResponseBody
    public Result getLiveURL(String shop_id, String device_id, String channel_idx, Integer upload_rate,
                             Integer live_mode, Integer enable_audio, Integer expire_time){
        Result result = new Result();
        LiveModel liveModel = liveService.getLiveUrl(liveurl,device_id,0);
        return result;
    }
}
