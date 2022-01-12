package com.shura.im.client.service.impl.command;

import com.shura.im.client.service.EchoService;
import com.shura.im.client.service.InnerCommand;
import com.shura.im.client.service.MsgHandle;
import com.shura.im.common.data.contruct.RingBufferWheel;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2022/1/12
 * @Description:
 */
@Service
public class DelayMsgCommand implements InnerCommand {

    @Autowired
    private EchoService echoService;

    @Autowired
    private MsgHandle msgHandle;

    @Autowired
    private RingBufferWheel ringBufferWheel;

    @Override
    public void process(String msg) {
        if (msg.split(" ").length <=2){
            echoService.echo("incorrect command, :delay [msg] [delayTime]");
            return ;
        }

        String message = msg.split(" ")[1];
        int delayTime = Integer.parseInt(msg.split(" ")[2]);

        RingBufferWheel.Task task = new DelayMsgJob(message);
        task.setKey(delayTime);
        ringBufferWheel.addTask(task);
        echoService.echo(EmojiParser.parseToUnicode(msg));
    }



    private class DelayMsgJob extends RingBufferWheel.Task{
        private String msg;

        public DelayMsgJob(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            msgHandle.sendMsg(msg);
        }
    }
}
