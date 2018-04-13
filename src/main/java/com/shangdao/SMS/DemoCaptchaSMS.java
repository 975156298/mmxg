package com.shangdao.SMS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.util.Assert;

public class DemoCaptchaSMS implements SMS {
    @Autowired
    RedisCacheManager cacheManager;

    @Override
    public void send(String mobile, String content) {
        cacheManager.getCache("mobileCode").put(mobile, "0000");

    }

    @Override
    public boolean verify(String code, String mobile) {
        Assert.notNull(code, "code 不能为 null");
        Assert.notNull(mobile, "mobile 不能为 null");
        String cacheCode = cacheManager.getCache("mobileCode").get(mobile, String.class);
        return code.equals(cacheCode);
    }
}
