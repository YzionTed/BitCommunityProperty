package com.bit.communityProperty.net;

import com.bit.communityProperty.bean.SkuAccountInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public class HttpsInterfaceManager {
    /**
     * 获取用户账户信息
     *
     * @param balanceType 3
     * @param callback
     */
    public static void getSkuAccountInfo(String balanceType, Callback<BaseResponse<SkuAccountInfo>> callback) {
        Map<String, String> maps = new HashMap<>();
        maps.put("balanceType", balanceType);
        Call<BaseResponse<SkuAccountInfo>> call = RetrofitManage.getInstance().getHttpServiceConnection()
                .getSkuAccountInfo(maps);
        call.enqueue(callback);
    }

    /**
     * 上传图片
     *
     * @param imagePath 图片路径
     * @param callback
     */
    public static void uploadImage(String imagePath, Callback<ResponseBody> callback) {
        File file = new File(imagePath);
        final RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        String descriptionString = "This is a description";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        Call<ResponseBody> call = RetrofitManage.getInstance().getHttpServiceConnection().uploadImage(description, body);
        call.enqueue(callback);
    }
}
