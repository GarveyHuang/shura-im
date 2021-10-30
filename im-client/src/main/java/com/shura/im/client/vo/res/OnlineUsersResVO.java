package com.shura.im.client.vo.res;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/24
 * @Description:
 */
public class OnlineUsersResVO implements Serializable {

    /**
     * code : 9000
     * message : 成功
     * reqNo : null
     * dataBody : [{"userId":1545574841528,"userName":"zhangsan"},{"userId":1545574871143,"userName":"tuling"}]
     */

    private String code;
    private String message;
    private Object reqNo;
    private List<DataBodyBean> dataBody;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReqNo() {
        return reqNo;
    }

    public void setReqNo(Object reqNo) {
        this.reqNo = reqNo;
    }

    public List<DataBodyBean> getDataBody() {
        return dataBody;
    }

    public void setDataBody(List<DataBodyBean> dataBody) {
        this.dataBody = dataBody;
    }

    public static class DataBodyBean {
        /**
         * userId : 1545574841528
         * userName : garvey
         */

        private long userId;
        private String username;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
