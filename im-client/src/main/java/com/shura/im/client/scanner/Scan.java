package com.shura.im.client.scanner;

import com.shura.im.client.service.EchoService;
import com.shura.im.client.service.MsgHandle;
import com.shura.im.client.service.MsgLoggerService;
import com.shura.im.client.util.SpringBeanFactory;
import com.vdurmont.emoji.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @Author: Garvey
 * @Created: 2021/11/21
 * @Description:
 */
public class Scan implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(Scan.class);

    private final MsgHandle msgHandle;

    private final MsgLoggerService msgLoggerService;

    private final EchoService echoService;

    public Scan() {
        this.msgHandle = SpringBeanFactory.getBean(MsgHandle.class);
        this.msgLoggerService = SpringBeanFactory.getBean(MsgLoggerService.class);
        this.echoService = SpringBeanFactory.getBean(EchoService.class);
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String msg = sc.nextLine();

            // 检查消息
            if (msgHandle.checkMsg(msg)) {
                continue;
            }

            // 系统内置命令
            if (msgHandle.innerCommand(msg)) {
                continue;
            }

            // 真正发送消息
            msgHandle.sendMsg(msg);

            // 写入聊天记录
            msgLoggerService.log(msg);

            echoService.echo(EmojiParser.parseToUnicode(msg));
        }
    }
}
