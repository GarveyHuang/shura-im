package com.shura.im.common.route.algorithm.consistenthash;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 一致性 hash 算法抽象类
 */
public abstract class AbstractConsistentHash {

    /**
     * 新增节点
     */
    protected abstract void add(long key, String value);

    /**
     * 排序节点，数据结构自身支持排序可以不用重写
     */
    protected void sort() {}

    /**
     * 根据当前 key 通过一致性 hash 算法的规则取出一个节点
     */
    protected abstract String getFirstNodeValue(String key);

    /**
     * 传入节点列表以及客户端信息获取一个服务节点
     */
    public String process(List<String> values, String key) {
        for (String value : values) {
            add(hash(value), value);
        }
        sort();

        return getFirstNodeValue(key);
    }

    /**
     * hash 运算
     */
    public Long hash(String value) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }

        md5.reset();
        byte[] keyBytes = null;
        keyBytes = value.getBytes(StandardCharsets.UTF_8);

        md5.update(keyBytes);
        byte[] digest = md5.digest();

        long hashCode = ((long) (digest[3] & 0xFF) << 24)
                | ((long) (digest[2] & 0xFF) << 16)
                | ((long) (digest[1] & 0xFF) << 8)
                | (digest[0] & 0xFF);

        return hashCode & 0xffffffffL;
    }
}
