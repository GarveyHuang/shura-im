package com.shura.im.gateway.service.impl;

import com.shura.im.common.pojo.IMUserInfo;
import com.shura.im.gateway.constant.Constants;
import com.shura.im.gateway.service.UserInfoCacheService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description: 客户端信息缓存 Service
 */
@Service("userInfoCacheService")
public class UserInfoCacheServiceImpl implements UserInfoCacheService {

    // TODO 本地缓存，为了防止内存被撑爆，后期改成 LRU
    private static final Map<Long, IMUserInfo> USER_INFO_MAP = new ConcurrentHashMap<>(64);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public IMUserInfo loadUserInfoByUserId(Long userId) {
        // 优先从本地缓存获取
        IMUserInfo userInfo = USER_INFO_MAP.get(userId);
        if (userInfo != null) {
            return userInfo;
        }

        // 从 Redis 缓存获取
        String sendUsername = redisTemplate.opsForValue().get(Constants.ACCOUNT_PREFIX + userId);
        if (StringUtils.isNotBlank(sendUsername)) {
            userInfo = new IMUserInfo(userId, sendUsername);
            USER_INFO_MAP.put(userId, userInfo);
        }

        return userInfo;
    }

    @Override
    public boolean saveAndCheckUserLoginStatus(Long userId) throws Exception {
        Long add = redisTemplate.opsForSet().add(Constants.LOGIN_STATUS_PREFIX, userId.toString());
        return !Objects.equals(add, 0L);
    }

    @Override
    public void removeLoginStatus(Long userId) throws Exception {
        redisTemplate.opsForSet().remove(Constants.LOGIN_STATUS_PREFIX, userId.toString());
    }

    @Override
    public Set<IMUserInfo> onlineUser() {
        Set<IMUserInfo> set = new HashSet<>(64);
        Set<String> members = redisTemplate.opsForSet().members(Constants.LOGIN_STATUS_PREFIX);
        for (String member : members) {
            IMUserInfo imUserInfo = loadUserInfoByUserId(Long.valueOf(member));
            set.add(imUserInfo);
        }
        return set;
    }
}
