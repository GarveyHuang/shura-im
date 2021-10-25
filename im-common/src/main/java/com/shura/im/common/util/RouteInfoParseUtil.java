package com.shura.im.common.util;

import com.shura.im.common.enums.StatusEnum;
import com.shura.im.common.exception.IMException;
import com.shura.im.common.pojo.RouteInfo;

/**
 * @Author: Garvey
 * @Created: 2021/10/23
 * @Description:
 */
public class RouteInfoParseUtil {

    public static RouteInfo parse(String info){
        try {
            String[] serverInfo = info.split(":");
            return new RouteInfo(serverInfo[0], Integer.parseInt(serverInfo[1]),Integer.parseInt(serverInfo[2]));
        }catch (Exception e){
            throw new IMException(StatusEnum.VALIDATION_FAIL) ;
        }
    }
}
