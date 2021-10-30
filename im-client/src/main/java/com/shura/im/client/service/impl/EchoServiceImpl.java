package com.shura.im.client.service.impl;

import com.shura.im.client.config.AppConfig;
import com.shura.im.client.service.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
@Service("echoService")
public class EchoServiceImpl implements EchoService {

    private static final String PREFIX = "$";

    @Autowired
    private AppConfig appConfig;

    @Override
    public void echo(String msg, Object... replace) {
        String date = LocalDate.now() + " " + LocalTime.now().withNano(0).toString();

        msg = "[" + date + "] \033[31;4m" + appConfig.getUserName() + PREFIX + "\033[0m" + " " + msg;

        String log = print(msg, replace);

        System.out.println(log);
    }


    /**
     * 打印消息
     *
     * @param msg
     * @param place
     * @return
     */
    private String print(String msg, Object... place) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (Object o : place) {
            int index = msg.indexOf("{}", k);

            if (index == -1) {
                return msg;
            }

            if (index != 0) {
                sb.append(msg, k, index);

            }
            sb.append(o);
            if (place.length == 1) {
                sb.append(msg, index + 2, msg.length());
            }

            k = index + 2;
        }
        if (sb.toString().equals("")) {
            return msg;
        } else {
            return sb.toString();
        }
    }
}
