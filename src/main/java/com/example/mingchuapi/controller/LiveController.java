package com.example.mingchuapi.controller;

import com.example.mingchuapi.model.CodeEnum;
import com.example.mingchuapi.model.LiveModel;
import com.example.mingchuapi.model.Result;
import com.example.mingchuapi.service.LiveService;
import com.example.mingchuapi.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 直播相关接口
 * @author ljx
 */
@RestController
public class LiveController {

    HttpUtils httpUtils;

    @Autowired
    LiveService liveService;




    /**
     * 平台获取设备直播地址。
     * @param shop_id 店铺唯一ID （食品经营许可证编码：JY + 14 位阿拉伯数字或CY + 13 位阿拉伯数字）
     * @param device_id 设备 ID
     * @param channel_idx 通道号
     * @param upload_rate 上传码率 （码流类型，0：高清；1：标清，默认 0）
     * @param live_mode 直播类型 （0：hls；1：flv；2：rtmp；默认：0）
     * @param enable_audio 启用音频 （0：否；1：是；默认：0）
     * @param expire_time 过期时长 （单位：秒；默认为 24 * 60 * 60 秒）
     * @return
     */
    @GetMapping("/live-address")
    public Result getLiveURL(String shop_id, String device_id, String channel_idx, @RequestParam( name = "upload_rate" , defaultValue = "0") Integer upload_rate,
                             @RequestParam( name = "upload_rate" , defaultValue = "0") Integer live_mode, @RequestParam( name = "enable_audio" , defaultValue = "0")Integer enable_audio,
                             @RequestParam( name = "expire_time" , defaultValue = "24") Integer expire_time){
       return liveService.getLiveUrl(device_id, 0);

    }

    @GetMapping("/playback-address")
    public Result getLiveBackURL(String shop_id, String device_id, String channel_idx, @RequestParam( name = "upload_rate" , defaultValue = "0") Integer upload_rate,
                                 @RequestParam( name = "upload_rate" , defaultValue = "0") Integer live_mode,@RequestParam( name = "play_type" , defaultValue = "0")Integer play_type,
                                 @RequestParam( name = "enable_audio" , defaultValue = "0")Integer enable_audio,Date start_time, Date stop_time,
                                 @RequestParam( name = "expire_time" , defaultValue = "24") Integer expire_time) {
        return liveService.getLiveBackUrl(device_id, start_time, stop_time);
    }

}
