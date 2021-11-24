package com.shura.im.client.controller;

import com.shura.im.client.client.IMClient;
import com.shura.im.client.service.RouteRequestService;
import com.shura.im.client.vo.req.GoogleProtocolVO;
import com.shura.im.client.vo.req.SendMsgReqVO;
import com.shura.im.client.vo.req.StringReqVO;
import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.res.BaseResponse;
import com.shura.im.common.res.NullBody;
import com.shura.im.gateway.api.vo.req.GroupReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Garvey
 * @Created: 2021/11/24
 * @Description:
 */
@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    private IMClient heartbeatClient;


    @Autowired
    private RouteRequestService routeRequestService;

    /**
     * 向服务端发消息 字符串
     *
     * @param stringReqVO
     * @return
     */
    @PostMapping(value = "sendStringMsg")
    public BaseResponse<NullBody> sendStringMsg(@RequestBody @Validated StringReqVO stringReqVO) {
        BaseResponse<NullBody> res = new BaseResponse<>();

        for (int i = 0; i < 100; i++) {
            heartbeatClient.sendStringMsg(stringReqVO.getMsg());
        }

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 向服务端发消息 Google ProtoBuf
     *
     * @param googleProtocolVO
     * @return
     */
    @PostMapping(value = "sendProtoBufMsg")
    public BaseResponse<NullBody> sendProtoBufMsg(@RequestBody @Validated GoogleProtocolVO googleProtocolVO) {
        BaseResponse<NullBody> res = new BaseResponse<>();

        for (int i = 0; i < 100; i++) {
            heartbeatClient.sendGoogleProtocolMsg(googleProtocolVO);
        }

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }


    /**
     * 群发消息
     *
     * @param sendMsgReqVO
     * @return
     */
    @RequestMapping(value = "sendGroupMsg")
    public BaseResponse<NullBody> sendGroupMsg(@RequestBody @Validated SendMsgReqVO sendMsgReqVO) throws Exception {
        BaseResponse<NullBody> res = new BaseResponse<>();

        GroupReqVO groupReqVO = new GroupReqVO(sendMsgReqVO.getUserId(), sendMsgReqVO.getMsg());
        routeRequestService.sendGroupMsg(groupReqVO);

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }
}
