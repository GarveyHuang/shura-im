package com.shura.im.gateway.exception;

import com.shura.im.common.exception.IMException;
import com.shura.im.common.res.BaseResponse;
import com.shura.im.common.res.NullBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Garvey
 * @Created: 2021/10/25
 * @Description: 统一异常处理
 */
@ControllerAdvice
public class ExceptionHandling {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandling.class);

    @ExceptionHandler(IMException.class)
    @ResponseBody
    public BaseResponse handleAllException(IMException ex) {
        LOGGER.error("exception", ex);
        BaseResponse<NullBody> baseResponse = new BaseResponse<>();
        baseResponse.setCode(ex.getErrorCode());
        baseResponse.setMessage(ex.getMessage());
        return baseResponse;
    }
}
