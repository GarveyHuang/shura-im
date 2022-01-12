package com.shura.im.client.service.impl.command;

import com.shura.im.client.client.IMClient;
import com.shura.im.client.component.ShutdownMsg;
import com.shura.im.client.service.*;
import com.shura.im.common.data.contruct.RingBufferWheel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2022/1/12
 * @Description:
 */
@Service
public class ShutDownCommand implements InnerCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShutDownCommand.class);

    @Autowired
    private RouteRequestService routeRequestService;

    @Autowired
    private IMClient imClient;

    @Autowired
    private MsgLoggerService msgLoggerService;

    @Resource(name = "callbackThreadPool")
    private ThreadPoolExecutor callBackExecutor;

    @Autowired
    private EchoService echoService;

    @Autowired
    private ShutdownMsg shutdownMsg;

    @Autowired
    private RingBufferWheel ringBufferWheel ;

    @Override
    public void process(String msg) {
        echoService.echo("tim client closing...");
        shutdownMsg.shutdown();
        routeRequestService.offLine();
        msgLoggerService.stop();
        callBackExecutor.shutdown();
        ringBufferWheel.stop(false);
        try {
            while (!callBackExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                echoService.echo("thread pool closing");
            }
            imClient.close();
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
        echoService.echo("im client close success!");
        System.exit(0);
    }
}
