package com.bit.communityProperty.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public class HttpUtil {
    public static Object getCallParameObject(RequestBody body, Class<?> parClass) throws IOException {
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = body.contentType();
        if (contentType != null){
            charset = contentType.charset(charset);
        }
        String paramsStr = buffer.readString(charset);
        Gson gson = new Gson();
        return gson.fromJson(paramsStr,parClass);
    }
}
