package com.IMeeting.entity;

/**
 * Created by gjw on 2018/11/19.
 */
public class ServerResult {
    // 返回状态，true,返回结果正确，false，返回结果异常
    private boolean status;
    // 返回的状态码
    private Integer code;
    // 返回的状态信息
    private String message;
    // 返回的数据
    private Object data;
    public ServerResult() {
        this.status = false;
        this.code = 0;
        this.message = null;
        this.data = null;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
