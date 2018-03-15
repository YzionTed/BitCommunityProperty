package com.bit.communityProperty.activity.faultDeclare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultManager.bean.FaultDetailBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.HomeMenuBean;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.GlideUtils;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.OssManager;
import com.bit.communityProperty.utils.PicSelectUtils;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.utils.UploadInfo;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.view.TitleBarView;
import com.bit.communityProperty.widget.NoScrollGridView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 新增故障的页面
 * Created by kezhangzhao on 2018/1/27.
 */

public class FaultAddActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private NoScrollGridView mNoScrollGridView;//图片评论网格布局
    private CommonAdapter mAdapter;//图片评论adapter
    private HomeMenuBean addPicture;//+号图片的bean类
    private int pictureNum = 0;//用来计算用的
    private ListView mTypeLv;//popup窗口里的ListView
    private List<String> faultTypeList;//故障类型列表
    private ArrayAdapter<String> faultDataAdapter;//故障类型列表适配器
    private PopupWindow typeSelectPopup;//popup窗口
    private Map<String, Integer> faultTypeMap = new HashMap<>();
    private TextView tvName, tvPhone;//名字、手机号
    private EditText edFaultType, edFaultAddress, edFaultInfo;//故障类型选择/故障地址/故障描述
    private Button btConfirm;//提交申请
    private UploadInfo uploadInfo;
    private ArrayList<String> imagUrlPar = new ArrayList<>();//上传图片地址列表，是个网络请求参数
    private PromptDialog uploadDialog;
    /**
     * 添加故障申请的请求参数
     * 1：水电煤气；2：房屋结构；3：消防安防；9：其它；10：电梯；11：门禁；99：其它；
     */
    private int faultType = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_add;
    }

    @Override
    public void initViewData() {
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("新增故障");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNoScrollGridView = findViewById(R.id.ns_grid);
        edFaultType = findViewById(R.id.ed_fault_type);
        edFaultType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击控件后显示popup窗口
                initSelectPopup();
                // 使用isShowing()检查popup窗口是否在显示状态
                if (typeSelectPopup != null && !typeSelectPopup.isShowing()) {
                    typeSelectPopup.showAsDropDown(edFaultType, 0, 10);
                }

            }
        });
        edFaultAddress = findViewById(R.id.ed_fault_address);
        edFaultInfo = findViewById(R.id.ed_fault_info);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        btConfirm = findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(new MyOnClickListener());
    }

    /**
     * 初始化popup窗口
     */
    private void initSelectPopup() {
        mTypeLv = new ListView(this);
        setData();
        // 设置适配器
        faultDataAdapter = new ArrayAdapter<String>(this, R.layout.popup_text_item, faultTypeList);
        mTypeLv.setAdapter(faultDataAdapter);

        // 设置ListView点击事件监听
        mTypeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 在这里获取item数据
                String value = faultTypeList.get(position);
                // 把选择的数据展示对应的TextView上
                faultType = faultTypeMap.get(value);
                edFaultType.setText(value);
                // 选择完后关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
        typeSelectPopup = new PopupWindow(mTypeLv, edFaultType.getWidth(), ActionBar.LayoutParams.WRAP_CONTENT, true);
        // 取得popup窗口的背景图片
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.bg_corner);
        typeSelectPopup.setBackgroundDrawable(drawable);
        typeSelectPopup.setFocusable(true);
        typeSelectPopup.setOutsideTouchable(true);
        typeSelectPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // 关闭popup窗口
                typeSelectPopup.dismiss();
            }
        });
    }

    /**
     * 模拟假数据
     */
    private void setData() {
        faultTypeList = new ArrayList<>();
        faultTypeList.add("水电煤气");
        faultTypeList.add("房屋结构");
        faultTypeList.add("消防安防");
        faultTypeList.add("其它");
        faultTypeList.add("电梯");
        faultTypeList.add("门禁");
        faultTypeList.add("其它");
        faultTypeMap.put("水电煤气", 1);
        faultTypeMap.put("房屋结构", 2);
        faultTypeMap.put("消防安防", 3);
        faultTypeMap.put("其它", 9);
        faultTypeMap.put("电梯", 10);
        faultTypeMap.put("门禁", 11);
        faultTypeMap.put("水电煤气", 99);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        initGridView();
        tvName.setText((String) SPUtil.get(mContext, AppConfig.name, ""));
        tvPhone.setText((String) SPUtil.get(mContext, AppConfig.phone, ""));
        uploadInfo = (UploadInfo) SPUtil.getObject(this, AppConfig.UPLOAD_INFO);
        if (uploadInfo == null || TimeUtils.isExpiration(uploadInfo.getExpiration())) {
            initOssToken();
        }
        uploadDialog = new PromptDialog(this);
    }

    /**
     * 初始化网格布局：图片评论
     */
    private void initGridView() {
        mAdapter = new CommonAdapter<HomeMenuBean>(mContext, R.layout.picture_comment_item) {
            @Override
            public void convert(ViewHolder holder, HomeMenuBean homeMenuBean, final int position, View convertView) {
                if (homeMenuBean.getmImageID() != 0) {
                    holder.setImageResource(R.id.iv_icon, homeMenuBean.getmImageID());
                } else {
                    try {
                        holder.setImageResource(R.id.iv_icon, Uri.fromFile(new File(homeMenuBean.getmImageUrl())));
                    } catch (Exception e) {

                    }
                }
                holder.setOnClickListener(R.id.grid_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position < 4 && pictureNum == position + 1) {
                            PicSelectUtils.startPhoto((Activity) mContext, 4 - position, PictureConfig.MULTIPLE);
                        }
                    }
                });
//                holder.setOnLongClickListener(R.id.grid_item, new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View view) {
//                        return true;
//                    }
//                });
            }
        };
        mNoScrollGridView.setAdapter(mAdapter);
        addPicture = new HomeMenuBean();//暂时用这个bean类
        addPicture.setmImageID(R.mipmap.add);
        List<HomeMenuBean> mainWorkBeanList = new ArrayList<>();
        mainWorkBeanList.add(addPicture);
        pictureNum = 1;
        mAdapter.setDatas(mainWorkBeanList);
    }

    /**
     * 图片地址转换，本地转换成云地址
     *
     * @param listPath
     */
    private void getImageHttpUrl(final List<HomeMenuBean> listPath) {
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < listPath.size(); i++) {
            listStr.add(listPath.get(i).getmImageUrl());
        }
        uploadInfo.setBucket(AppConfig.BUCKET_NAME);
        OssManager.getInstance().uploadFileToAliYun(uploadInfo, listStr, imagUrlPar, new OssManager.UploadFinishListener() {
            @Override
            public void uploadFinish(List<String> finalName) {
                if (!TextUtils.isEmpty(edFaultAddress.getText()) && !TextUtils.isEmpty(edFaultInfo.getText()))
                    getFaultDetail("5a82adf3b06c97e0cd6c0f3d", 2, faultType, edFaultAddress.getText().toString(), edFaultInfo.getText().toString(), imagUrlPar);

            }
        });
    }

    /**
     * 网络请求添加故障申请
     *
     * @param communityId       房间ID
     * @param type         故障类型 1：住户；2：公共；
     * @param faultItem    故障项目 1：水电煤气；2：房屋结构；3：消防安防；9：其它；10：电梯；11：门禁；99：其它；
     * @param faultAddress 故障地址
     * @param faultContent 故障描述
     * @param pictureList  图片列表
     */
    private void getFaultDetail(String communityId, int type, int faultItem, String faultAddress, String faultContent, ArrayList pictureList) {
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", communityId);
        map.put("faultType", type);
        map.put("faultItem", faultItem);
        map.put("faultAddress", faultAddress);
        map.put("faultContent", faultContent);
        map.put("faultAccessory", pictureList);

        RetrofitManage.getInstance().subscribe(Api.getInstance().addFault(map), new Observer<BaseEntity<FaultDetailBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<FaultDetailBean> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                uploadDialog.showSuccess("上传成功");
                if (baseEntity.getData() != null && baseEntity.isSuccess()) {
//                    mFaultDetailBean = baseEntity.getData();
//                    setViewDate(mFaultDetailBean);
                    uploadDialog.showSuccess("上传成功");
                    finish();
                }else {
                    uploadDialog.showSuccess("上传失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                uploadDialog.dismiss();
            }

            @Override
            public void onComplete() {
                uploadDialog.dismiss();
            }
        });
    }

    /**
     * 点击事件监听
     */
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_confirm:
                    if (TextUtils.isEmpty(edFaultType.getText())) {
                        ToastUtil.showShort("请选择故障类型");
                    } else if (TextUtils.isEmpty(edFaultAddress.getText())) {
                        ToastUtil.showShort("请填写故障地址信息");
                    } else if (TextUtils.isEmpty(edFaultInfo.getText())) {
                        ToastUtil.showShort("请填写故障详情信息");
                    } else if (mAdapter.getDatas() != null) {
                        uploadDialog.showLoading("上传图片中...");
                        List<HomeMenuBean> listPathzs = mAdapter.getDatas();
                        if (mAdapter.getDatas().size()<4) {//因为小于4张的时候会有个加号的图片在尾部，要去除
                            listPathzs.remove(listPathzs.size() - 1);
                        }else if (mAdapter.getDatas().size()==4){
                            HomeMenuBean bean = (HomeMenuBean) mAdapter.getDatas().get(3);
                            if (TextUtils.isEmpty(bean.getmImageUrl())){
                                listPathzs.remove(3);
                            }
                        }
                        getImageHttpUrl(listPathzs);
                    } else {
                        ToastUtil.showShort("填写信息有误");
                    }
                    break;
            }
        }
    }

    private void initOssToken() {
        RetrofitManage.getInstance().subscribe(Api.getInstance().ossToken(), new Observer<BaseEntity<UploadInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<UploadInfo> uploadInfoBaseEntity) {
                if (uploadInfoBaseEntity.isSuccess()) {
                    uploadInfo = uploadInfoBaseEntity.getData();
                    SPUtil.saveObject(mContext, AppConfig.UPLOAD_INFO, uploadInfo);
                    if (uploadInfo != null) {
                        OssManager.getInstance().init(mContext, uploadInfo);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    List<HomeMenuBean> list = new ArrayList<>();
                    for (int i = 0; i < selectList.size(); i++) {
                        HomeMenuBean bean = new HomeMenuBean();
                        String rulstr = selectList.get(i).getCompressPath();
                        bean.setmImageUrl(rulstr);
                        list.add(bean);

                    }
                    mAdapter.remove(pictureNum - 1);
                    pictureNum = pictureNum + list.size();
                    if (pictureNum < 5)
                        list.add(addPicture);
                    mAdapter.addDatas(list);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
//                    if (uploadInfo != null) {
//                        uploadDialog.showLoading("上传图片中...");
//                        uploadInfo.setBucket("bit-test");
//                        imgUrl = OssManager.getInstance().uploadFileToAliYun(uploadInfo, selectList.get(0).getPath(), new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//                            @Override
//                            public void onIsSuccess(PutObjectRequest ossRequest, PutObjectResult ossResult) {
////                                uploadDialog.showSuccess("上传成功");
////                                uploadDialog.dismiss();
//                                addClock();
//                            }
//
//                            @Override
//                            public void onFailure(PutObjectRequest ossRequest, ClientException e, ServiceException e1) {
//                                uploadDialog.dismiss();
//                                ToastUtil.showShort("上传失败");
//                            }
//                        });
//                    }
                    break;
            }
        }
    }
}
