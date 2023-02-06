package com.example.mingchuapi.controller;

import com.example.mingchuapi.model.Result;
import com.example.mingchuapi.service.LiveService;
import com.example.mingchuapi.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 直播相关接口
 *
 * @author ljx
 */
@Api("直播相关接口")
@RestController
public class LiveController {

    HttpUtils httpUtils;

    @Autowired
    LiveService liveService;


    /**
     * 平台获取设备直播地址。
     *
     * @param shop_id      店铺唯一ID （食品经营许可证编码：JY + 14 位阿拉伯数字或CY + 13 位阿拉伯数字）
     * @param device_id    设备 ID
     * @param channel_idx  通道号
     * @param upload_rate  上传码率 （码流类型，0：高清；1：标清，默认 0）
     * @param live_mode    直播类型 （0：hls；1：flv；2：rtmp；默认：0）
     * @param enable_audio 启用音频 （0：否；1：是；默认：0）
     * @param expire_time  过期时长 （单位：秒；默认为 24 * 60 * 60 秒）
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "device_id", value = "设备 ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "shop_id", value = "店铺唯一 ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "channel_idx", value = "通道号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "upload_rate", value = "上传码率 （码流类型，0：高清；1：标清，默认 0）", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "live_mode", value = "直播类型 （0：hls；1：flv；2：rtmp；默认：0）", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "enable_audio", value = "启用音频（0：否；1：是；默认：0）", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "expire_time", value = "过期时长（单位：秒；默认为 24 * 60 * 60 秒）", paramType = "query", dataType = "Integer")
    })
    @ApiOperation(value = "直播地址获取接口")
    @GetMapping("/live-address")
    public Result getLiveURL(String shop_id, String device_id, String channel_idx, @RequestParam(name = "upload_rate", defaultValue = "0") Integer upload_rate,
                             @RequestParam(name = "upload_rate", defaultValue = "0") Integer live_mode, @RequestParam(name = "enable_audio", defaultValue = "0") Integer enable_audio,
                             @RequestParam(name = "expire_time", defaultValue = "24") Integer expire_time) {
        return liveService.getLiveUrl(device_id, 0);

    }


    @ApiOperation(value = "获取设备历史回放本地视频地址接口")
    @GetMapping("/playback-address")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "device_id", value = "设备 ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "start_time", value = "视频开始时间（2023-02-06 16:03:00）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "stop_time", value = "视频结束时间（2023-02-06 17:30:12）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "shop_id", value = "店铺唯一 ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "channel_idx", value = "通道号", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "live_mode", value = "直播类型 （0：hls；1：flv；2：rtmp；默认：0）", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "enable_audio", value = "启用音频（0：否；1：是；默认：0）", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "expire_time", value = "过期时长（单位：秒；默认为 24 * 60 * 60 秒）", paramType = "query", dataType = "Integer")
    })
    public Result getLiveBackURL(String shop_id, String device_id, String channel_idx, @RequestParam(name = "upload_rate", defaultValue = "0") Integer upload_rate,
                                 @RequestParam(name = "upload_rate", defaultValue = "0") Integer live_mode, @RequestParam(name = "play_type", defaultValue = "0") Integer play_type,
                                 @RequestParam(name = "enable_audio", defaultValue = "0") Integer enable_audio, String start_time, String stop_time,
                                 @RequestParam(name = "expire_time", defaultValue = "24") Integer expire_time) {
        return liveService.getLiveBackUrl(device_id, start_time, stop_time);
    }

}
