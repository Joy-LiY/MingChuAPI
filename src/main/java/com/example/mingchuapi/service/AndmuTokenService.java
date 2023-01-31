package com.example.mingchuapi.service;

import org.springframework.stereotype.Component;

@Component
public class AndmuTokenService {
//	@Autowired
//	private RedisCache redisCache;
//
//	private final int expireTime = 7 * 24 * 60;
//
//	public String createToken() {
//		AndmuResult result = AndmuUtils.getToken();
//		String token = result.getResult();
//		if(StringUtils.isNotEmpty(token)) {
//			redisCache.setCacheObject(Constants.ANDMU_TOKEN_KEY, token, expireTime, TimeUnit.MINUTES);
//		}
//		return token;
//	}
//
//	public String getToken() {
//		String token = redisCache.getCacheObject(Constants.ANDMU_TOKEN_KEY);
//		if (StringUtils.isEmpty(token)) {
//			token = createToken();
//		}
//		return token;
//	}
}
