package com.example.mingchuapi.service;

import com.example.mingchuapi.model.AndmuResult;
import com.example.mingchuapi.util.AndmuUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author taozi
 */
@Component
@Slf4j
public class AndmuTokenService {
    // 全局保存token值
    private static String token = " ";

    /**
     * 创建Token值
     *
     * @return
     */
    public void createToken() {
        AndmuResult andmuResult = AndmuUtils.getToken();
        token = andmuResult.getResult();
        log.info("AndmuTokenService创建token：{}", token);
    }

    /**
     * 获取token的值
     *
     * @return
     */
    public String getToken() {
        if (StringUtils.isBlank(token)) {
            this.createToken();
        }
        log.info("AndmuTokenService返回token：{}", token);
        return token;
    }
}
