package com.example.mingchuapi.controller;

import com.example.mingchuapi.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zhangwentao
 * @CreateTime: 2023/1/31
 * @Description: 设备信息相关接口
 * @Version: 1.0
 */
@Api("手机归属地")
@RestController
public class DeviceController {
    @Resource
    private DeviceService deviceService;

    /**
     * 平台监测设备健康状态。
     *
     * @param shopId   店铺唯一 ID
     * @param deviceId 设备 ID 必填
     * @return Object
     * 设备在线状态
     * 1：在线；2：离线
     */
    @ApiOperation(value = "平台监测设备健康状态（1：在线；2：离线）", notes = "平台监测设备健康状态（1：在线；2：离线）")
    @GetMapping(value = "/device-status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shop_id", value = "店铺唯一 ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "device_id", value = "设备 ID", required = true, paramType = "query", dataType = "String")})
    public Object getDeviceStatus(@RequestParam(name = "shop_id", required = false) String shopId,
                                  @RequestParam(name = "device_id") String deviceId,
                                  HttpServletResponse response) {
        return deviceService.getDeviceStatus(shopId, deviceId, response);
    }


    /**
     * @param channelIdx 通道号
     * @param deviceId   设备 ID
     * @param shopId     店铺唯一 ID
     * @description: 截取指定设备图片
     * @author: zhangwentao
     * @date: 2023/1/31 下午1:48
     * @param: [shopId, deviceId, channelIdx]
     * @return: Object
     **/
    @ApiOperation(value = "截取指定设备图片", notes = "截取指定设备图片")
    @GetMapping(value = "/device-screenshot")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shop_id", value = "店铺唯一 ID", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "device_id", value = "设备 ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "channel_idx", value = "通道号", paramType = "query", dataType = "String")})
    public Object getScreenshot(@RequestParam(name = "shop_id", required = false) String shopId,
                                @RequestParam(name = "device_id") String deviceId,
                                @RequestParam(name = "channel_idx", required = false) String channelIdx,
                                HttpServletResponse response) {
        return deviceService.getScreenshot(shopId, deviceId, channelIdx, response);
    }
}
