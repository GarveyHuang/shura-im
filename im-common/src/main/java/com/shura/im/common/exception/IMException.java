package com.shura.im.common.exception;

import com.shura.im.common.enums.StatusEnum;

/**
 * @Author: Garvey
 * @Created: 2021/10/22
 * @Description:
 */
public class IMException extends GenericException {

    public IMException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public IMException(Exception e, String errorCode, String errorMessage) {
        super(e, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public IMException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public IMException(StatusEnum statusEnum) {
        super(statusEnum.getMessage());
        this.errorMessage = statusEnum.message();
        this.errorCode = statusEnum.getCode();
    }

    public IMException(StatusEnum statusEnum, String message) {
        super(message);
        this.errorMessage = message;
        this.errorCode = statusEnum.getCode();
    }

    public IMException(Exception oriEx) {
        super(oriEx);
    }

    public IMException(Throwable oriEx) {
        super(oriEx);
    }

    public IMException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }

    public IMException(String message, Throwable oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
    }


    public static boolean isResetByPeer(String msg) {
        if ("Connection reset by peer".equals(msg)) {
            return true;
        }
        return false;
    }
}
