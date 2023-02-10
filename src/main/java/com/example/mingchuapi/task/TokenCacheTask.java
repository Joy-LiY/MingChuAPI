package com.example.mingchuapi.task;

import com.example.mingchuapi.service.AndmuTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    private static final long RATE = 7 * 23 * 60 * 60 * 1000L;

    @PreDestroy
    public void destroy() {
        //系统运行结束
        log.info("========>系统关闭中=====================>");
    }

    /**
     * @description: token的有效期限为7天，这里设置为失效前1个小时重新获取
     * @author: zhangwentao
     * @date: 2023/1/31 下午3:07
     * @param: []
     * @return: void
     **/
    @Scheduled(fixedDelay = RATE)
    public void tokenScheduled() {
        log.info("===============>获取token的定时任务已启动获取间隔为:{}ms", RATE);
        andmuTokenService.createToken();
    }
}