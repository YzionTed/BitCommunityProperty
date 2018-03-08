package com.bit.communityProperty.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 服务端请求解析/回调<br>
 * <p>
 * 默认采用Gson解析数据，model可以没有setter<br>
 * 重载{@link #onSuccess(Object)} 方法获取解析结果<br>
 * 重载{@link #onFailure(ServiceException)} 方法处理请求失败<br>
 * 重载{@link #parseData(JsonElement)} 方法以自定义解析data数据<br>
 * 如果不需要解析data数据，泛型用 {@link Void} 或 {@link Object}<br>
 * 默认解析方法里，泛型如果是JsonElement/JsonObject/JsonArray，那么直接返回，这样不必每个请求都建model<br>
 * <p>
 * 之所以用abstract，是为了防止直接构造，必须是子类的形式，才能取到泛型的类型<br>
 */

/**
 * Created by zhangjiajie on 2015/11/19.
 *
 * @param <T>
 */
public abstract class ResponseCallBack<T> {

    public static final String FIELD_CODE = "errorCode";
    public static final String FIELD_MSG = "errorMsg";
    public static final String FIELD_DATA = "data";

    /**
     * 是否解析第一层为code/msg/data
     */
    protected final boolean mFlag;

    /**
     * T 的数据类型
     */
    protected final Type mType;

    /**
     * {@link #mFlag} = true
     */
    public ResponseCallBack() {
        this(true);
    }

    /**
     * @param flag {@link #mFlag}
     */
    public ResponseCallBack(boolean flag) {
        mFlag = flag;
        mType = getType();
    }

    /**
     * 请求解析成功
     *
     * @param data 解析数据
     */
    public void onSuccess(T data) {
    }

    /**
     * 请求失败
     *
     * @param e 异常
     */
    public void onFailure(ServiceException e) {
    }

    // 这里是照搬 com.google.gson.reflect.TypeToken 的代码，但是直接用 mType = new TypeToken<T>() {}.getType() 又不行。
    final /*package*/ Type getType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        // 调用gson内部类对Type进行整理
        try {
            Class<?> gt = Class.forName("com.google.gson.internal.$Gson$Types");
            Method method = gt.getMethod("canonicalize", Type.class);
            return (Type) method.invoke(null, parameterized.getActualTypeArguments()[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //处理第一层解析
    final /*package*/ T parse(String str) throws Exception {
        JsonObject jo;
        try {
            JsonElement je = new JsonParser().parse(str);
            if (!mFlag) {
                return parseData(je);
            }
            jo = je.getAsJsonObject();
            int stateCode = jo.getAsJsonPrimitive(FIELD_CODE).getAsInt();
            if (stateCode != 0) { // 检查服务端返回的状态码,如果不为0则返回错误
                String stateDesc = jo.getAsJsonPrimitive(FIELD_MSG).getAsString();
                ServiceException serviceException = new ServiceException(stateCode, stateDesc);
                ExceptionHandler.handle(serviceException);
                throw serviceException;
            }
        } catch (RuntimeException e) {
            throw new ServiceException(e);
        }

        if (mType == Void.class || mType == Object.class) {
            return null;    //不解析data
        }

        if (mType == String.class) {
            return (T) jo.get(FIELD_DATA).toString();
        }

        if (jo.has(FIELD_DATA)) {
            return parseData(jo.get(FIELD_DATA));   // 解析data字段
        } else {
            return null;
        }
    }

    /**
     * 重载此方法以自定义解析data数据
     *
     * @param data
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public T parseData(JsonElement data) throws Exception {
        if (mType == JsonElement.class || mType == JsonObject.class || mType == JsonArray.class) {
            return (T) data;
        }
        Gson gson = new GsonBuilder().registerTypeAdapter(int.class, new IntegerDeserializer())
                .registerTypeAdapter(Integer.class, new IntegerDeserializer()).create();
        return gson.fromJson(data, mType);
    }

}


/**
 * Gson Integer类型反序列化类,用于处理Gson默认实现中类型转换错误的问题
 */
class IntegerDeserializer implements JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonElement je, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            return je.getAsInt();
        } catch (NumberFormatException ex) {
            try {
                String string = je.getAsString();
                if (!TextUtils.isEmpty(string)) {
                    string = string.replaceAll(",", "");
                    if (string.indexOf('.') > 0) {
                        string = string.substring(0, string.indexOf('.'));
                    }
                    return Integer.parseInt(string);
                }
            } catch (Exception e) {
            }
            throw new JsonParseException(ex);
        }
    }

}