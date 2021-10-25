package com.shura.im.common.route.algorithm.random;

import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.exception.IMException;
import com.shura.im.common.route.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author: Garvey
 * @Created: 2021/10/25
 * @Description: 路由策略，随机
 */
public class RandomHandle implements RouteHandle {

    @Override
    public String routeServer(List<String> values, String key) {
        if (values == null || values.size() == 0) {
            throw new IMException(StatusEnum.SERVER_NOT_AVAILABLE);
        }

        int offset = ThreadLocalRandom.current().nextInt(values.size());

        return values.get(offset);
    }
}
