package com.shura.im.client.service.impl.command;

import com.shura.im.client.component.ClientInfo;
import com.shura.im.client.service.EchoService;
import com.shura.im.client.service.InnerCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2022/1/12
 * @Description:
 */
@Service
public class EchoInfoCommand implements InnerCommand {

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String msg) {
        echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        echoService.echo("client info={}", clientInfo.get().getUserName());
        echoService.echo("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
