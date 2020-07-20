package com.zslin.bus.common.tools;

import com.zslin.basic.tools.SecurityUtil;
import com.zslin.cache.CacheTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

/**
 * 移动端用户登陆工具类
 */
@Component
public class AppUserLoginTools {

    @Autowired
    private CacheTools cacheTools;

    /**
     * 检测Token是否有效
     * @param username
     * @param token
     * @return
     */
    public boolean checkLogin(String username, String token) {
        if(token.equalsIgnoreCase("noLogin")) {return true;}
        String oldToken = (String) cacheTools.getKey(username);
        if(oldToken==null || !token.equals(oldToken)) {return false;}
        else {return true;}
    }

    /**
     * 生成token
     * @param username
     * @return
     */
    public String buildToken(String username) {
        try {
            Long time = System.currentTimeMillis();
            cacheTools.deleteKey(username); //操作之前先清除原有缓存
            String token = SecurityUtil.md5(username, time+"");
            cacheTools.putKey(username, token, 3600 * 24 * 365); //存一年
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return RandomTools.randomString(16);
        }
    }
}
