package com.bit.communityProperty.net;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public class BaseResponse<T>implements Serializable {
    private boolean success;
    @SerializedName("errorCode")
    public String errorCode;     // 返回码
    @SerializedName("description")
    public String description;  //结果描述
    @SerializedName("data")
    public T data;       // 数据

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

   /* public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
