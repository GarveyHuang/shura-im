package com.shura.im.client.service;

import com.shura.im.client.vo.res.IMServerResVO;
import com.shura.im.client.vo.res.OnlineUsersResVO;
import com.shura.im.gateway.api.vo.req.GroupReqVO;
import com.shura.im.gateway.api.vo.req.LoginReqVO;
import com.shura.im.gateway.api.vo.req.Person2PersonReqVO;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
public interface RouteRequestService {

    /**
     * 群发消息
     * @param groupReqVO 消息
     * @throws Exception
     */
    void sendGroupMsg(GroupReqVO groupReqVO) throws Exception;


    /**
     * 私聊
     * @param p2PReqVO
     * @throws Exception
     */
    void sendP2PMsg(Person2PersonReqVO p2PReqVO)throws Exception;

    /**
     * 获取服务器
     * @return 服务ip + port
     * @param loginReqVO
     * @throws Exception
     */
    IMServerResVO.ServerInfo getTIMServer(LoginReqVO loginReqVO) throws Exception;

    /**
     * 获取所有在线用户
     * @return
     * @throws Exception
     */
    List<OnlineUsersResVO.DataBodyBean> onlineUsers()throws Exception;


    void offLine();
}
