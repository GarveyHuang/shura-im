package com.shura.im.client.service.impl;

import com.shura.im.client.component.InnerCommandContext;
import com.shura.im.client.config.AppConfig;
import com.shura.im.client.service.InnerCommand;
import com.shura.im.client.service.MsgHandle;
import com.shura.im.client.service.MsgLoggerService;
import com.shura.im.client.service.RouteRequestService;
import com.shura.im.gateway.api.vo.req.GroupReqVO;
import com.shura.im.gateway.api.vo.req.Person2PersonReqVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
@Service("msgHandle")
public class MsgHandler implements MsgHandle {

    private final static Logger LOGGER = LoggerFactory.getLogger(MsgHandler.class);

    private boolean aiModel = false;

    @Autowired
    private RouteRequestService routeRequestService;

    @Autowired
    private AppConfig appConfig;

    @Resource(name = "callbackThreadPool")
    private ThreadPoolExecutor executor;

    @Autowired
    private MsgLoggerService msgLoggerService;

    @Autowired
    private InnerCommandContext innerCommandContext;

    @Override
    public void sendMsg(String msg) {
        if (aiModel) {
            aiChat(msg);
        } else {
            normalChat(msg);
        }
    }

    /**
     * 正常聊天
     *
     * @param msg
     */
    private void normalChat(String msg) {
        String[] totalMsg = msg.split("::");
        if (totalMsg.length > 1) {
            //私聊
            Person2PersonReqVO p2PReqVO = new Person2PersonReqVO();
            p2PReqVO.setUserId(appConfig.getUserId());
            p2PReqVO.setReceiveUserId(Long.parseLong(totalMsg[0]));
            p2PReqVO.setMsg(totalMsg[1]);
            try {
                p2pChat(p2PReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }

        } else {
            //群聊
            GroupReqVO groupReqVO = new GroupReqVO(appConfig.getUserId(), msg);
            try {
                groupChat(groupReqVO);
            } catch (Exception e) {
                LOGGER.error("Exception", e);
            }
        }
    }

    /**
     * AI model
     *
     * @param msg
     */
    private void aiChat(String msg) {
        msg = msg.replace("吗", "");
        msg = msg.replace("嘛", "");
        msg = msg.replace("?", "!");
        msg = msg.replace("？", "!");
        msg = msg.replace("你", "我");
        System.out.println("AI:\033[31;4m" + msg + "\033[0m");
    }

    @Override
    public void groupChat(GroupReqVO groupReqVO) throws Exception {
        routeRequestService.sendGroupMsg(groupReqVO);
    }

    @Override
    public void p2pChat(Person2PersonReqVO p2PReqVO) throws Exception {
        routeRequestService.sendP2PMsg(p2PReqVO);

    }

    @Override
    public boolean checkMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            LOGGER.warn("不能发送空消息！");
            return false;
        }
        return true;
    }

    @Override
    public boolean innerCommand(String msg) {
        if (msg.startsWith(":")) {
            InnerCommand instance = innerCommandContext.getInstance(msg);
            instance.process(msg);

            return true;
        } else {
            return false;
        }

    }

    /**
     * 关闭系统
     */
    @Override
    public void shutdown() {
        LOGGER.info("系统关闭中。。。。");
        routeRequestService.offLine();
        msgLoggerService.stop();
        executor.shutdown();
        try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                LOGGER.info("线程池关闭中。。。。");
            }
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException", e);
        }
        System.exit(0);
    }

    @Override
    public void openAIModel() {
        aiModel = true;
    }

    @Override
    public void closeAIModel() {
        aiModel = false;
    }
}
