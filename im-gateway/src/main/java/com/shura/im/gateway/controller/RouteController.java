package com.shura.im.gateway.controller;

import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.exception.IMException;
import com.shura.im.common.pojo.IMUserInfo;
import com.shura.im.common.pojo.RouteInfo;
import com.shura.im.common.res.BaseResponse;
import com.shura.im.common.res.NullBody;
import com.shura.im.common.route.algorithm.RouteHandle;
import com.shura.im.common.util.RouteInfoParseUtil;
import com.shura.im.gateway.api.RouteApi;
import com.shura.im.gateway.api.vo.req.GroupReqVO;
import com.shura.im.gateway.api.vo.req.LoginReqVO;
import com.shura.im.gateway.api.vo.req.Person2PersonReqVO;
import com.shura.im.gateway.api.vo.req.RegisterInfoReqVO;
import com.shura.im.gateway.api.vo.res.IMServerResVO;
import com.shura.im.gateway.api.vo.res.RegisterInfoResVO;
import com.shura.im.gateway.cache.ServerCache;
import com.shura.im.gateway.service.AccountService;
import com.shura.im.gateway.service.CommonBizService;
import com.shura.im.gateway.service.UserInfoCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description: 路由 Controller
 */
@RestController
@RequestMapping("/")
public class RouteController implements RouteApi {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private ServerCache serverCache;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Autowired
    private CommonBizService commonBizService;

    @Autowired
    private RouteHandle routeHandle;

    /**
     * 群聊 API
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "groupRoute")
    @Override
    public BaseResponse<NullBody> groupRoute(@RequestBody GroupReqVO groupReqVO) throws Exception {
        BaseResponse<NullBody> res = new BaseResponse();

        LOGGER.info("msg=[{}]", groupReqVO.toString());

        // 获取所有的推送列表
        Map<Long, IMServerResVO> serverResVOMap = accountService.loadRouteRelated();
        for (Map.Entry<Long, IMServerResVO> timServerResVOEntry : serverResVOMap.entrySet()) {
            Long userId = timServerResVOEntry.getKey();
            IMServerResVO TIMServerResVO = timServerResVOEntry.getValue();
            if (userId.equals(groupReqVO.getUserId())) {
                // 过滤掉自己
                IMUserInfo timUserInfo = userInfoCacheService.loadUserInfoByUserId(groupReqVO.getUserId());
                LOGGER.warn("过滤掉了发送者 userId={}", timUserInfo.toString());
                continue;
            }

            //推送消息
            GroupReqVO chatVO = new GroupReqVO(userId, groupReqVO.getMsg());
            accountService.pushMsg(TIMServerResVO, groupReqVO.getUserId(), chatVO);
        }

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }


    /**
     * 私聊 API
     *
     * @param p2pRequest
     * @return
     */
    @PostMapping(value = "p2pRoute")
    @Override
    public BaseResponse<NullBody> p2pRoute(@RequestBody Person2PersonReqVO p2pRequest) throws Exception {
        BaseResponse<NullBody> res = new BaseResponse();

        try {
            //获取接收消息用户的路由信息
            IMServerResVO serverResVO = accountService.loadRouteRelatedByUserId(p2pRequest.getReceiveUserId());

            // p2pRequest.getReceiveUserId() ==> 消息接收者的 userID
            GroupReqVO chatVO = new GroupReqVO(p2pRequest.getReceiveUserId(), p2pRequest.getMsg());
            accountService.pushMsg(serverResVO, p2pRequest.getUserId(), chatVO);

            res.setCode(StatusEnum.SUCCESS.getCode());
            res.setMessage(StatusEnum.SUCCESS.getMessage());

        } catch (IMException e) {
            res.setCode(e.getErrorCode());
            res.setMessage(e.getErrorMessage());
        }
        return res;
    }

    /**
     * 客户端下线
     *
     * @param groupReqVO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "offLine")
    @Override
    public BaseResponse<NullBody> offLine(@RequestBody GroupReqVO groupReqVO) throws Exception {
        BaseResponse<NullBody> res = new BaseResponse();

        IMUserInfo timUserInfo = userInfoCacheService.loadUserInfoByUserId(groupReqVO.getUserId());

        LOGGER.info("user [{}] offline!", timUserInfo.toString());
        accountService.offLine(groupReqVO.getUserId());

        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 登录并获取一台 IM server
     *
     * @return
     */
    @PostMapping(value = "login")
    @Override
    public BaseResponse<IMServerResVO> login(@RequestBody LoginReqVO loginReqVO) throws Exception {
        BaseResponse<IMServerResVO> res = new BaseResponse();

        // 登录校验
        StatusEnum status = accountService.login(loginReqVO);
        if (status == StatusEnum.SUCCESS) {

            // 从 zookeeper 里挑选一台客户端需访问的 netty 服务器
            String server = routeHandle.routeServer(serverCache.getServerList(), String.valueOf(loginReqVO.getUserId()));
            LOGGER.info("username=[{}] route server info=[{}]", loginReqVO.getUsername(), server);

            // 检查服务的可用性
            RouteInfo routeInfo = RouteInfoParseUtil.parse(server);
            commonBizService.checkServerAvailable(routeInfo);

            // 保存路由信息
            accountService.saveRouteInfo(loginReqVO, server);

            IMServerResVO vo = new IMServerResVO(routeInfo);
            res.setDataBody(vo);

        }
        res.setCode(status.getCode());
        res.setMessage(status.getMessage());

        return res;
    }

    /**
     * 注册账号
     *
     * @return
     */
    @PostMapping(value = "registerAccount")
    @Override
    public BaseResponse<RegisterInfoResVO> registerAccount(@RequestBody RegisterInfoReqVO registerInfoReqVO) throws Exception {
        BaseResponse<RegisterInfoResVO> res = new BaseResponse();

        long userId = System.currentTimeMillis();
        RegisterInfoResVO info = new RegisterInfoResVO(userId, registerInfoReqVO.getUsername());
        info = accountService.register(info);

        res.setDataBody(info);
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }

    /**
     * 获取所有在线用户
     *
     * @return
     */
    @PostMapping(value = "onlineUser")
    @Override
    public BaseResponse<Set<IMUserInfo>> onlineUser() throws Exception {
        BaseResponse<Set<IMUserInfo>> res = new BaseResponse();

        Set<IMUserInfo> userInfoSet = userInfoCacheService.onlineUser();
        res.setDataBody(userInfoSet);
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        return res;
    }
}
