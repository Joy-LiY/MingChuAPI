package com.example.mingchuapi.task;

import com.example.mingchuapi.model.AndmuResult;
import com.example.mingchuapi.util.AndmuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @Author: zhangwentao
 * @CreateTime: 2023/1/30
 * @Description: 获取Token信息的定时任务
 * @Version: 1.0
 */
@Component
@Slf4j
public class TokenCacheTask {
    public static String token = " ";


    @PostConstruct
    public void init() {
        log.info("=======>系统启动中，开始获取token -----");
        AndmuResult andmuResult = AndmuUtils.getToken();
        TokenCacheTask.token = andmuResult.getResult();
        log.info("=======>系统启动中，token获取完成{}", token);
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