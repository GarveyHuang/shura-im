package com.shura.im.client.service.impl.command;

import com.shura.im.client.service.EchoService;
import com.shura.im.client.service.InnerCommand;
import com.shura.im.client.service.MsgLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2022/1/12
 * @Description:
 */
@Service
public class QueryHistoryCommand implements InnerCommand {

    @Autowired
    private MsgLoggerService msgLoggerService;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String msg) {
        String[] split = msg.split(" ");
        if (split.length < 2){
            return;
        }
        String res = msgLoggerService.query(split[1]);
        echoService.echo(res);
    }
}
