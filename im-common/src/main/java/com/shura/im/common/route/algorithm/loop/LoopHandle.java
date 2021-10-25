package com.shura.im.common.route.algorithm.loop;

import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.exception.IMException;
import com.shura.im.common.route.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Garvey
 * @Created: 2021/10/25
 * @Description: 路由策略，轮询
 */
public class LoopHandle implements RouteHandle {

    private AtomicInteger index = new AtomicInteger();

    @Override
    public String routeServer(List<String> values, String key) {
        if (values == null || values.size() == 0) {
            throw new IMException(StatusEnum.SERVER_NOT_AVAILABLE);
        }

        int position = index.incrementAndGet() % values.size();
        if (position < 0) {
            position = 0;
        }

        return values.get(position);
    }
}
