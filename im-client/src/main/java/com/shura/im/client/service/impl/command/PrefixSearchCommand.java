package com.shura.im.client.service.impl.command;

import com.shura.im.client.service.EchoService;
import com.shura.im.client.service.InnerCommand;
import com.shura.im.client.service.RouteRequestService;
import com.shura.im.client.vo.res.OnlineUsersResVO;
import com.shura.im.common.data.contruct.TrieTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2022/1/12
 * @Description:
 */
@Service
public class PrefixSearchCommand implements InnerCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(PrefixSearchCommand.class);

    @Autowired
    private RouteRequestService routeRequestService;
    @Autowired
    private EchoService echoService;

    @Override
    public void process(String msg) {
        try {
            List<OnlineUsersResVO.DataBodyBean> onlineUsers = routeRequestService.onlineUsers();
            TrieTree trieTree = new TrieTree();
            for (OnlineUsersResVO.DataBodyBean onlineUser : onlineUsers) {
                trieTree.insert(onlineUser.getUsername());
            }

            String[] split = msg.split(" ");
            String key = split[1];
            List<String> list = trieTree.prefixSearch(key);

            for (String res : list) {
                res = res.replace(key, "\033[31;4m" + key + "\033[0m");
                echoService.echo(res);
            }

        } catch (Exception e) {
            LOGGER.error("Exception", e);
        }
    }
}
