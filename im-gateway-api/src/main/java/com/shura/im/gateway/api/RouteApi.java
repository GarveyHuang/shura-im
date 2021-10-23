package com.shura.im.gateway.api;

import com.shura.im.common.res.BaseResponse;
import com.shura.im.gateway.api.vo.req.GroupReqVO;
import com.shura.im.gateway.api.vo.req.LoginReqVO;
import com.shura.im.gateway.api.vo.req.Person2PersonReqVO;
import com.shura.im.gateway.api.vo.req.RegisterInfoReqVO;
import com.shura.im.gateway.api.vo.res.RegisterInfoResVO;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description: 路由 API
 */
public interface RouteApi {

    /**
     * group chat
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object groupRoute(GroupReqVO groupReqVO) throws Exception;

    /**
     * Point to point chat
     *
     * @param p2pRequest
     * @return
     * @throws Exception
     */
    Object p2pRoute(Person2PersonReqVO p2pRequest) throws Exception;


    /**
     * Offline account
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    Object offLine(GroupReqVO groupReqVO) throws Exception;

    /**
     * Login account
     *
     * @param loginReqVO
     * @return
     * @throws Exception
     */
    Object login(LoginReqVO loginReqVO) throws Exception;

    /**
     * Register account
     *
     * @param registerInfoReqVO
     * @return
     * @throws Exception
     */
    BaseResponse<RegisterInfoResVO> registerAccount(RegisterInfoReqVO registerInfoReqVO) throws Exception;

    /**
     * Get all online users
     *
     * @return
     * @throws Exception
     */
    Object onlineUser() throws Exception;
}
