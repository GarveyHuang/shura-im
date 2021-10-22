package com.shura.im.server.api;

import com.shura.im.server.api.vo.req.SendMsgReqVO;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 通讯服务端 API 接口
 */
public interface ServerApi {

    /**
     * 发送消息到客户端
     * @param sendMsgReqVO
     * @return
     * @throws Exception
     */
    Object sendMsg(SendMsgReqVO sendMsgReqVO) throws Exception;
}
