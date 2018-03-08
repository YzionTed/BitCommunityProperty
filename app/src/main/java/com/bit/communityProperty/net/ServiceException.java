package com.bit.communityProperty.net;

/**
 * Created by zhangjiajie on 2015/11/19.
 */
public class ServiceException extends Exception {

    /**
     * -1:为返回结果格式错误,为空或者格式不符合Json;
     * 0: 调用成功
     * 其余则按照后台定义
     */
    private int code;

    public ServiceException() {
        super();
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return getMessage();
    }

}
