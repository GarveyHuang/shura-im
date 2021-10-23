package com.shura.im.server.util;

import com.shura.im.common.pojo.IMUserInfo;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
public class SessionSocketHolder {

    private static final Map<Long, NioSocketChannel> CHANNEL_MAP = new ConcurrentHashMap<>(16);

    private static final Map<Long, String> SESSION_MAP = new ConcurrentHashMap<>(16);

    public static void saveSession(Long userId, String userName) {
        SESSION_MAP.put(userId, userName);
    }

    public static void removeSession(Long userId) {
        SESSION_MAP.remove(userId);
    }

    /**
     * 保存关系
     *
     * @param id
     * @param socketChannel
     */
    public static void put(Long id, NioSocketChannel socketChannel) {
        CHANNEL_MAP.put(id, socketChannel);
    }

    public static NioSocketChannel get(Long id) {
        return CHANNEL_MAP.get(id);
    }

    public static Map<Long, NioSocketChannel> getRelationShip() {
        return CHANNEL_MAP;
    }

    public static void remove(NioSocketChannel nioSocketChannel) {
        CHANNEL_MAP.entrySet().stream().filter(entry -> entry.getValue() == nioSocketChannel).forEach(entry -> CHANNEL_MAP.remove(entry.getKey()));
    }

    /**
     * 获取注册用户信息
     *
     * @param nioSocketChannel
     * @return
     */
    public static IMUserInfo getUserId(NioSocketChannel nioSocketChannel) {
        for (Map.Entry<Long, NioSocketChannel> entry : CHANNEL_MAP.entrySet()) {
            NioSocketChannel value = entry.getValue();
            if (nioSocketChannel == value) {
                Long key = entry.getKey();
                String userName = SESSION_MAP.get(key);
                return new IMUserInfo(key, userName);
            }
        }

        return null;
    }
}
