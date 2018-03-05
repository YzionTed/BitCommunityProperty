package com.bit.communityProperty.bean;

/**
 * Created by 23 on 2018/2/8.
 */

public class MessageEvent {
    private boolean isLoginSuccess;

    public boolean isLoginSuccess() {
        return isLoginSuccess;
    }
    private String eventmessage;

    public String getEventmessage() {
        return eventmessage;
    }

    public void setEventmessage(String eventmessage) {
        this.eventmessage = eventmessage;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        isLoginSuccess = loginSuccess;
    }
}
