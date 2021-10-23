package com.shura.im.server.controller;

import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.res.BaseResponse;
import com.shura.im.server.api.ServerApi;
import com.shura.im.server.api.vo.req.SendMsgReqVO;
import com.shura.im.server.api.vo.res.SendMsgResVO;
import com.shura.im.server.server.IMServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
@RestController
@RequestMapping("/")
public class IndexController implements ServerApi {

    @Autowired
    private IMServer imServer;

    @Override
    @PostMapping("sendMsg")
    public BaseResponse<SendMsgResVO> sendMsg(SendMsgReqVO sendMsgReqVO) throws Exception {
        BaseResponse<SendMsgResVO> res = new BaseResponse<>();
        imServer.sendMsg(sendMsgReqVO);

        SendMsgResVO sendMsgResVO = new SendMsgResVO();
        sendMsgResVO.setMsg("OK");

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        res.setDataBody(sendMsgResVO);
        return res;
    }
}
