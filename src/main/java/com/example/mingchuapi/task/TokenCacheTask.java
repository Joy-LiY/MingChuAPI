package com.example.mingchuapi.task;

import com.example.mingchuapi.service.AndmuTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @Author: zhangwentao
 * @CreateTime: 2023/1/30
 * @Description: 获取Token信息的定时任务
 * @Version: 1.0
 */
@Component
@Slf4j
public class TokenCacheTask {
    @Resource
    private AndmuTokenService andmuTokenService;


    @PostConstruct
    public void init() {
        log.info("=======>系统启动中，开始获取token========>");
        andmuTokenService.createToken();
        log.info("=======>系统启动中，token获取完成========>");
    }

    @PreDestroy
    public void destroy() {
        //系统运行结束
    }

    @Scheduled(cron = "0 0 0/2 * * ?")
    public void testOne() {
        //每2小时执行一次缓存
        init();
    }
}