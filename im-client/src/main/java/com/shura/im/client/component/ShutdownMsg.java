package com.shura.im.client.component;

import org.springframework.stereotype.Component;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
@Component
public class ShutdownMsg {

    private boolean isCommand ;

    /**
     * 置为用户主动退出状态
     */
    public void shutdown(){
        isCommand = true ;
    }

    public boolean checkStatus(){
        return isCommand ;
    }
}
