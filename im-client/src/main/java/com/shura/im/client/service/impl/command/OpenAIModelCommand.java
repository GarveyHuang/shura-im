package com.shura.im.client.service.impl.command;

import com.shura.im.client.service.InnerCommand;
import com.shura.im.client.service.MsgHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2022/1/12
 * @Description:
 */
@Service
public class OpenAIModelCommand implements InnerCommand {

    @Autowired
    private MsgHandle msgHandle;

    @Override
    public void process(String msg) {
        msgHandle.openAIModel();
        System.out.println("\033[31;4m" + "Hello，我是估值过亿的 AI 机器人！" + "\033[0m");
    }
}
