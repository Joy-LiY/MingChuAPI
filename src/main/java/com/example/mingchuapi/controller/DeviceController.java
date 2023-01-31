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
     * @param shop_id   店铺唯一 ID
     * @param device_id 设备 ID 必填
     * @return Result
     * 设备在线状态
     * 1：在线；2：离线
     */
    @GetMapping(value = "/device-status")
    public Result getDeviceStatus(@RequestParam(name = "shop_id", required = false) String shop_id, @RequestParam(name = "device_id") String device_id) {
        return deviceService.getDeviceStatus(shop_id, device_id);
    }
}
