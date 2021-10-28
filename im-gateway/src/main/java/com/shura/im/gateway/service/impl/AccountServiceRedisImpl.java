package com.shura.im.gateway.service.impl;

import com.shura.im.common.core.proxy.ProxyManager;
import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.exception.IMException;
import com.shura.im.common.pojo.IMUserInfo;
import com.shura.im.common.util.RouteInfoParseUtil;
import com.shura.im.gateway.api.vo.req.GroupReqVO;
import com.shura.im.gateway.api.vo.req.LoginReqVO;
import com.shura.im.gateway.api.vo.res.IMServerResVO;
import com.shura.im.gateway.api.vo.res.RegisterInfoResVO;
import com.shura.im.gateway.constant.Constants;
import com.shura.im.gateway.service.AccountService;
import com.shura.im.gateway.service.UserInfoCacheService;
import com.shura.im.server.api.ServerApi;
import com.shura.im.server.api.vo.req.SendMsgReqVO;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description: 账号 Service 实现类
 */
@Service("accountService")
public class AccountServiceRedisImpl implements AccountService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountServiceRedisImpl.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private OkHttpClient okHttpClient;

    @Override
    public RegisterInfoResVO register(RegisterInfoResVO info) throws Exception {
        String key = Constants.ACCOUNT_PREFIX + info.getUserId();

        String username = redisTemplate.opsForValue().get(info.getUsername());
        if (StringUtils.isBlank(username)) {
            // 为了方便查询，冗余一份
            redisTemplate.opsForValue().set(key, info.getUsername());
            redisTemplate.opsForValue().set(info.getUsername(), key);
        } else {
            long userId = Long.parseLong(username.split(":")[1]);
            info.setUserId(userId);
            info.setUsername(username);
        }

        return info;
    }

    @Override
    public StatusEnum login(LoginReqVO loginReqVO) throws Exception {
        // 去 Redis 里查询
        String key = Constants.ACCOUNT_PREFIX + loginReqVO.getUserId();
        String username = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(username)) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        if (!username.equals(loginReqVO.getUsername())) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }

        // 登录成功，保存登录状态
        boolean status = userInfoCacheService.saveAndCheckUserLoginStatus(loginReqVO.getUserId());
        if (!status) {
            return StatusEnum.REPEAT_LOGIN;
        }

        return StatusEnum.SUCCESS;
    }

    @Override
    public void saveRouteInfo(LoginReqVO loginReqVO, String msg) throws Exception {
        String key = Constants.ROUTE_PREFIX + loginReqVO.getUserId();
        redisTemplate.opsForValue().set(key, msg);
    }

    @Override
    public Map<Long, IMServerResVO> loadRouteRelated() {
        Map<Long, IMServerResVO> routes = new HashMap<>(64);

        RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
        ScanOptions options = ScanOptions.scanOptions()
                .match(Constants.ROUTE_PREFIX + "*")
                .build();
        Cursor<byte[]> scan = redisConnection.scan(options);

        while (scan.hasNext()) {
            byte[] next = scan.next();
            String key = new String(next, StandardCharsets.UTF_8);
            LOGGER.info("key={}", key);

            parseServerInfo(routes, key);
        }

        try {
            scan.close();
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
        return routes;
    }

    @Override
    public IMServerResVO loadRouteRelatedByUserId(Long userId) {
        String value = redisTemplate.opsForValue().get(Constants.ACCOUNT_PREFIX + userId);
        if (StringUtils.isBlank(value)) {
            throw new IMException(StatusEnum.OFF_LINE);
        }

        return new IMServerResVO(RouteInfoParseUtil.parse(value));
    }

    @Override
    public void pushMsg(IMServerResVO imServerResVO, long sendUserId, GroupReqVO groupReqVO) throws Exception {
        IMUserInfo userInfo = userInfoCacheService.loadUserInfoByUserId(sendUserId);

        String url = "http://" + imServerResVO.getIp() + ":" + imServerResVO.getHttpPort();
        ServerApi serverApi = new ProxyManager<>(ServerApi.class, url, okHttpClient).getInstance();
        SendMsgReqVO vo = new SendMsgReqVO(userInfo.getUsername() + ":" + groupReqVO.getMsg(), groupReqVO.getUserId());
        Response response = null;
        try {
            response = (Response) serverApi.sendMsg(vo);
        } catch (Exception e) {
            LOGGER.error("Exception", e);
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
    }

    @Override
    public void offLine(Long userId) throws Exception {
        // TODO 这里需要使用 lua 保证操作的原子性，后续优化
        // 删除路由
        redisTemplate.delete(Constants.ROUTE_PREFIX + userId);

        // 删除登录状态
        userInfoCacheService.removeLoginStatus(userId);
    }

    private void parseServerInfo(Map<Long, IMServerResVO> routes, String key) {
        long userId = Long.valueOf(key.split(":")[1]);
        String value = redisTemplate.opsForValue().get(key);
        IMServerResVO serverResVO = new IMServerResVO(RouteInfoParseUtil.parse(value));
        routes.put(userId, serverResVO);
    }
}
