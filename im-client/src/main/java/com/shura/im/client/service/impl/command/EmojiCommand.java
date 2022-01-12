package com.shura.im.client.service.impl.command;

import com.shura.im.client.service.EchoService;
import com.shura.im.client.service.InnerCommand;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2022/1/12
 * @Description:
 */
@Service
public class EmojiCommand implements InnerCommand {

    @Autowired
    private EchoService echoService;

    @Override
    public void process(String msg) {
        if (msg.split(" ").length <=1){
            echoService.echo("incorrect command, :emoji [option]");
            return ;
        }
        String value = msg.split(" ")[1];
        if (value != null) {
            int index = Integer.parseInt(value);
            List<Emoji> all = (List<Emoji>) EmojiManager.getAll();
            all = all.subList(5 * index, 5 * index + 5);

            for (Emoji emoji : all) {
                echoService.echo(EmojiParser.parseToAliases(emoji.getUnicode()) + "--->" + emoji.getUnicode());
            }
        }

    }
}
