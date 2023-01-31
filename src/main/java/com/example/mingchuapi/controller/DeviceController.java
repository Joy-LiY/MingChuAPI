package com.example.mingchuapi.controller;

import com.example.mingchuapi.model.Result;
import com.example.mingchuapi.service.DeviceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zhangwentao
 * @CreateTime: 2023/1/31
 * @Description: 设备信息相关接口
 * @Version: 1.0
 */
@RestController
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    /**
     * 平台监测设备健康状态。
     *
     * @param shopId   店铺唯一 ID
     * @param deviceId 设备 ID 必填
     * @return Result
     * 设备在线状态
     * 1：在线；2：离线
     */
    @GetMapping(value = "/device-status")
    public Result getDeviceStatus(@RequestParam(name = "shop_id", required = false) String shopId,
                                  @RequestParam(name = "device_id") String deviceId) {
        return deviceService.getDeviceStatus(shopId, deviceId);
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
    @GetMapping(value = "/device-screenshot")
    public Result getScreenshot(@RequestParam(name = "shop_id", required = false) String shopId,
                                @RequestParam(name = "device_id") String deviceId,
                                @RequestParam(name = "channel_idx", required = false) String channelIdx) {
        return deviceService.getScreenshot(shopId, deviceId, channelIdx);
    }
}
