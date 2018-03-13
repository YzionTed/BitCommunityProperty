package com.bit.communityProperty.net;

import com.bit.communityProperty.activity.cleanclock.bean.CleanClockListBean;
import com.bit.communityProperty.activity.deviceManagement.bean.CameraBean;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeBean;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeDetailBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBeanPar;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlDetailBean;
import com.bit.communityProperty.activity.deviceManagement.bean.ElevatorDetailBean;
import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.bit.communityProperty.activity.faultManager.bean.AssignPersonBean;
import com.bit.communityProperty.activity.faultManager.bean.FaultDetailBean;
import com.bit.communityProperty.activity.faultManager.bean.FaultManagementBean;
import com.bit.communityProperty.activity.household.bean.AuditedBean;
import com.bit.communityProperty.activity.household.bean.AuditedUserBean;
import com.bit.communityProperty.activity.household.bean.AuditingBean;
import com.bit.communityProperty.activity.houseinfo.bean.HouseInfoBean;
import com.bit.communityProperty.activity.releasePass.bean.ReleasePassDetailsBean;
import com.bit.communityProperty.activity.safetywarning.bean.AlarmListBean;
import com.bit.communityProperty.activity.userinfo.bean.UserData;
import com.bit.communityProperty.activity.workplan.bean.PersonalWorkListBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.AppVersionInfo;
import com.bit.communityProperty.bean.CardListBean;
import com.bit.communityProperty.bean.DoorMILiBean;
import com.bit.communityProperty.bean.LoginData;
import com.bit.communityProperty.bean.OnlineData;
import com.bit.communityProperty.bean.PublicKeybean;
import com.bit.communityProperty.bean.SkuAccountInfo;
import com.bit.communityProperty.fragment.main.bean.BannerBean;
import com.bit.communityProperty.fragment.main.bean.MainNewsBean;
import com.bit.communityProperty.fragment.main.bean.MainNewsDetailBean;
import com.bit.communityProperty.fragment.main.bean.OwnerApplyNumBean;
import com.bit.communityProperty.fragment.main.bean.WeatherInfoBean;
import com.bit.communityProperty.utils.UploadInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public interface HttpService {

    // 用户登录
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("sku/user/login")
    Observable<BaseResponse<String>> userLogin(@FieldMap() Map<String, String> maps);
//    Observable<BaseResponse<String>> userLogin(@Body RequestBody route);

    // 上传图片
    @Multipart
    @POST("s?cl=3&tn=baidutop10&fr=top1000&wd=%E5%88%98%E4%BA%A6%E8%8F%B2%E6%B7%B1%E5%A4%9C%E5%8F%91%E6%96%87&rsv_idx=2")
    Call<ResponseBody> uploadImage(@Part("description") RequestBody description,
                                   @Part MultipartBody.Part file);

    // 获取用户账户信息
    @FormUrlEncoded
    @POST("s?cl=3&tn=baidutop10&fr=top1000&wd=%E5%88%98%E4%BA%A6%E8%8F%B2%E6%B7%B1%E5%A4%9C%E5%8F%91%E6%96%87&rsv_idx=2")
    Call<BaseResponse<SkuAccountInfo>> getSkuAccountInfo(@FieldMap() Map<String, String> maps);

    //获取公钥
    @POST("sku/common/getPublicKey")
    Call<BaseResponse<PublicKeybean>> getPublicKey();

    /***
     * 获取验证码
     */
    @POST("/v1/user/getVerificationCode")
    Call<String> getVerificationCode(@Path("phone") String phone);

    /***
     * 意见反馈
     */

    @POST("/v1/sys/feedback/add")
    Observable<BaseEntity<Object>> Add(@Body Map<String, String> maps);

    /***
     * 登陆
     */

    @POST("/v1/user/signIn")
    Observable<BaseEntity<LoginData>> signIn(@Body Map<String, String> maps);

    /***
     * 退出登录
     */
    @GET("/v1/user/signOut")
    Observable<BaseEntity<String>> signOut();

    @GET("/v1/user/signOut")
    Observable<BaseEntity<String>> signOut(@Header("BIT_TOKEN") String BIT_TOKEN, @Header("BIT_UID") String BIT_UID);

    /**
     * 获取当前用户信息
     */

    @GET("/v1/user/curr")
    Observable<BaseEntity<UserData>> GetCurrUser();

    /**
     * 获取小区详情
     *
     * @param id
     * @return
     */
    @GET("/v1/community/{id}/detail")
    Observable<BaseEntity<HouseInfoBean>> Detail(@Path("id") String id);

    /**
     * 修改用户信息
     */

    @POST("/v1/user/edit")
    Observable<BaseEntity<String>> EditUser();

    @Headers("DEVICE_TYPE:Android")
    @GET("v1/oss/sts-token")
    Observable<BaseEntity<UploadInfo>> getPicToken(@Header("BIT_TOKEN") String BIT_TOKEN, @Header("BIT_UID") String BIT_UID);

    /**
     * 获取门禁记录
     *
     * @param deviceBeanPar
     * @return
     */
    @Headers("DEVICE_TYPE:Android")
    @POST("v1/communityIoT/door/page")
    Observable<BaseEntity<DeviceBean>> getDoorRecord(@Body DeviceBeanPar deviceBeanPar);

    /**
     * 获取报警列表
     *
     * @param maps
     * @return
     */
    @POST("/v1/property/alarm/getAlarm")
    Observable<BaseEntity<AlarmListBean>> getAlarmList(@Body Map<String, Object> maps);

    /**
     * 保安接警
     *
     * @param map
     * @return
     */
    @POST("/v1/property/alarm/receiveAlarm")
    Observable<BaseEntity<Object>> receiveAlarm(@Body Map<String, Object> map);

    /**
     * 接警排查
     *
     * @return
     */
    @POST("/v1/property/alarm/troubleShoot")
    Observable<BaseEntity<Object>> troubleShoot(@Body Map<String, Object> map);

    /**
     * 获取电梯列表
     *
     * @param map
     * @return
     */
    @POST("v1/communityIoT/elevator/get/list")
    Observable<BaseEntity<List<ElevatorListBean>>> elevatorList(@Body Map<String, Object> map);

    /**
     * 添加打卡记录
     *
     * @param map
     * @return
     */
    @POST("/v1/task/record/add")
    Observable<BaseEntity<Object>> addClock(@Body Map<String, Object> map);

    /**
     * 获取oss的token
     *
     * @return
     */
    @GET("/v1/oss/sts-token")
    Observable<BaseEntity<UploadInfo>> ossToken();

    /**
     * 获取打卡记录列表
     *
     * @param map
     * @return
     */
    @GET("/v1/task/record/page")
    Observable<BaseEntity<CleanClockListBean>> getCleanClockList(@QueryMap Map<String, Object> map);

    /**
     * 获取监控设备列表
     *
     * @param map
     * @return
     */
    @POST("v1/communityIoT/camera/auth/page")
    Observable<BaseEntity<CameraBean>> getMonitorList(@Body Map<String, Object> map);

    /**
     * 获取门禁设备列表
     *
     * @param map
     * @return
     */
    @POST("v1/communityIoT/door/page")
    Observable<BaseEntity<DoorControlBean>> getDoorControlList(@Body Map<String, Object> map);

    /**
     * 设备管理-获取门禁使用记录
     *
     * @param map
     * @return
     */
    @POST("/v1/sys/door-record/page")
    Observable<BaseEntity<DoorControlDetailBean>> getDoorUseList(@Body Map<String, Object> map);

    /**
     * 放行条详情
     *
     * @param url "/v1/property/rpass/{id}/detail"
     * @return
     */
    @GET
    Observable<BaseEntity<ReleasePassDetailsBean>> getReleasePassInfo(@Url String url);

    /**
     * 获取业主申请数量
     *
     * @param url /v1/user/{communityId}/count-unreviewed-proprietors
     * @return
     */
    @GET
    Observable<BaseEntity<OwnerApplyNumBean>> getOwnerApplyNum(@Url String url);

    /**
     * 在线客服
     * ("/v1/user/property/{communityId}/user-list")
     */

    @GET
    Observable<BaseEntity<ArrayList<OnlineData>>> online(@Url String url, @QueryMap Map<String, Object> map);

    /**
     * 住户管理模块
     * 按社区获取用户列表
     *
     * @param url v1/user/{communityId}/getByCommunityId
     * @return
     */
    @GET
    Observable<BaseEntity<AuditingBean>> getAuditingList(@Url String url, @QueryMap Map<String, Object> map);

    /**
     * 根据社区统计各楼宇有效业主数量
     *
     * @param url "/v1/user/{communityId}/proprietors-statistics"
     * @return
     */
    @GET
    Observable<BaseEntity<ArrayList<AuditedBean>>> getHouseholdNum(@Url String url);

    /**
     * 按楼宇ID获取用户关系列表
     *
     * @param url "/v1/user/{buildingId}/by-building-id
     * @return
     */
    @GET
    Observable<BaseEntity<AuditedUserBean>> getUsersByBuildingId(@Url String url, @QueryMap Map<String, Object> map);

    /**
     * 审核房屋认证
     * （0：未审核；1：审核通过；-1：驳回；-2：违规; 2: 已注销; 3: 已解绑;）
     *
     * @param map
     * @return
     */
    @POST("/v1/user/property/audit")
    Observable<BaseEntity<String>> auditingHouseUser(@Body Map<String, Object> map);


    /**
     * 审批放行条
     *
     * @param map
     * @return
     */
    @POST("/v1/property/rpass/check")
    Observable<BaseEntity<ReleasePassDetailsBean>> approvalPass(@Body Map<String, Object> map);

    /**
     * 获取排班列表
     *
     * @return
     */
    @POST("/v1/task/schedule/list")
    Observable<BaseEntity<List<PersonalWorkListBean>>> getWorkList(@Body Map<String, Object> map);

    /**
     * 天气信息
     *
     * @return
     */
    @POST("/v1/communityIoT/weather/city/view")
    Observable<BaseEntity<WeatherInfoBean>> getWeatherInfo(@Body Map<String, Object> map);

    /**
     * 分页获取小区公告列表
     *
     * @return
     */
    @POST("/v1/property/notice/page")
    Observable<BaseEntity<MainNewsBean>> getNoticeList(@Body Map<String, Object> map);

    /**
     * 获取小区公告详情
     *
     * @param id
     * @return
     */
    @GET("/v1/property/notice/{id}/detail")
    Observable<BaseEntity<MainNewsDetailBean>> getNoticeDetail(@Path("id") String id);


    /**
     * 获取门禁列表
     *
     * @param
     * @return
     */
    @POST("/v1/communityIoT/door/auth/list")
    Observable<BaseEntity<List<DoorMILiBean>>> getDoorAuthList(@Body Map<String, Object> map);

    /**
     * 经过后台请求，得到电梯物联网的电梯数据
     *
     * @param
     * @return
     */
    @POST("/v1/communityIoT/elevator/get/auth/list")
    Observable<BaseEntity<List<ElevatorListBean>>> getDoorGetAuthsList(@Body Map<String, Object> map);

    /**
     * 获取版本更新
     *
     * @param appId
     * @param sequence
     * @return
     */
    @GET("/v1/sys/{appId}/version/{sequence}/new")
    Observable<BaseEntity<AppVersionInfo>> getVersion(@Path("appId") String appId, @Path("sequence") String sequence);

    /**
     * 设备管理-获取车闸列表
     *
     * @return
     */
    @POST("/v1/vehicle/car-gate/page")
    Observable<BaseEntity<CarBrakeBean>> getCarGateList(@Query("page") int page, @Query("size") int size);

    /**
     * 设备管理-获取车闸进出详情
     *
     * @param map
     * @return
     */
    @POST("/v1/vehicle/inout/page")
    Observable<BaseEntity<CarBrakeDetailBean>> getCarBrakeDetail(@Body Map<String, Object> map);

    /**
     * 设备管理-获取电梯列表
     *
     * @return
     */
    @POST("/v1/communityIoT/elevator/list")
    Observable<BaseEntity<com.bit.communityProperty.activity.deviceManagement.bean.ElevatorListBean>> getElevatorList(@Body Map<String, Object> map);

    /**
     * 设备管理-获取电梯使用记录
     */
    @POST("/v1/sys/elevator-record/page")
    Observable<BaseEntity<ElevatorDetailBean>> getElevatorUseList(@Body Map<String, Object> map);

    /**
     * 获取轮播图
     *
     * @param map
     * @return
     */
    @POST("/v1/sys/slide/list")
    Observable<BaseEntity<List<BannerBean>>> getBanner(@Body Map<String, Object> map);

    /**
     * 获取故障列表
     *
     * @param map
     * @return
     */
    @Headers("DEVICE_TYPE:Android")
    @POST("v1/property/fault/queryFaultPage")
    Observable<BaseEntity<FaultManagementBean>> getFaultlList(@Body Map<String, Object> map);

    /**
     * 获取故障设备详情
     *
     * @param id
     * @return
     */
    @GET("v1/property/fault/{id}/detail")
    Observable<BaseEntity<FaultDetailBean>> getFaultDetail(@Path("id") String id);

    /**
     * 添加故障申请
     *
     * @param map
     * @return
     */
    @Headers("DEVICE_TYPE:Android")
    @POST("v1/property/fault/addFault")
    Observable<BaseEntity<FaultDetailBean>> addFault(@Body Map<String, Object> map);

    /**
     * 删除故障申报单
     *
     * @param id
     * @return
     */
    @GET("v1/property/fault/{id}/delete")
    Observable<BaseEntity<String>> deleteFault(@Path("id") String id);

    /**
     * 住户评论故障申请
     *
     * @param map
     * @return
     */
    @Headers("DEVICE_TYPE:Android")
    @POST("v1/property/fault/comment")
    Observable<BaseEntity<String>> evaluateFault(@Body Map<String, Object> map);


    /**
     * 物业根据岗位获取人员列表
     * @param communityId
     * @param map
     * @return
     */
    @GET("v1/user/property/{communityId}/user-list")
    Observable<BaseEntity<ArrayList<AssignPersonBean>>> getPersonnelList(@Path("communityId") String communityId,
                                                              @QueryMap Map<String, Object> map);

    /**
     * 为故障单分配维修人员
     * @param map
     * @return
     */
    @Headers("DEVICE_TYPE:Android")
    @POST("v1/property/fault/allocation")
    Observable<BaseEntity<FaultDetailBean>> submitAssign(@Body Map<String, Object> map);


    /**
     * 处理故障报修单
     *
     * @param map
     * @return
     */
    @Headers("DEVICE_TYPE:Android")
    @POST("v1/property/fault/editFaultStatus")
    Observable<BaseEntity<FaultDetailBean>> handleFault(@Body Map<String, Object> map);


    /**
     * 查询虚拟卡片
     *
     * @param map
     * @return
     */
    @POST("/v1/user/card/get/list")
    Observable<BaseEntity<CardListBean>> getCardList(@Body Map<String, Object> map);
}
