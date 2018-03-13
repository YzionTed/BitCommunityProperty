package com.bit.communityProperty.activity.cleanclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.cleanclock.adapter.CleanClockListAdapter;
import com.bit.communityProperty.activity.cleanclock.bean.CleanClockListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.net.ThrowableUtils;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.OssManager;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.TimeUtils;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.utils.UploadInfo;
import com.classic.common.MultipleStatusView;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.leefeng.promptlibrary.PromptDialog;

public class CleanClockListActivity extends BaseActivity {

    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_right_action_bar)
    TextView btnRightActionBar;
    @BindView(R.id.iv_right_action_bar)
    ImageView ivRightActionBar;
    @BindView(R.id.pb_loaing_action_bar)
    ProgressBar pbLoaingActionBar;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.lv_clean)
    LRecyclerView lvClean;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private CleanClockListAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private Map<String, Object> map = new HashMap<>();
    private Map<String, Object> listMap = new HashMap<>();
    private UploadInfo uploadInfo;
    private String imgUrl;//图片url
    private PromptDialog uploadDialog;
    private int page = 1;
    private boolean isRefresh = true;
    private CleanClockListBean cleanClockListBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_clean_clock_list;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("保洁打卡");
        btnRightActionBar.setText("打卡");
        btnRightActionBar.setVisibility(View.VISIBLE);
        uploadDialog = new PromptDialog(this);
        multipleStatusView.showLoading();
        uploadInfo = (UploadInfo) SPUtil.getObject(this, AppConfig.UPLOAD_INFO);
        if (uploadInfo == null || TimeUtils.isExpiration(uploadInfo.getExpiration())) {
            initOssToken();
        }
        initDate();
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleStatusView.showLoading();
                isRefresh = true;
                getList();
            }
        });
    }

    private void initDate() {
        adapter = new CleanClockListAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lvClean.setAdapter(mLRecyclerViewAdapter);
        lvClean.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration divider = new DividerDecoration.Builder(mContext)
                .setHeight(R.dimen.common_dp_10)
                .setColorResource(R.color.appbg)
                .build();
        lvClean.addItemDecoration(divider);
        lvClean.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getList();
            }
        });


        lvClean.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (cleanClockListBean.getCurrentPage() < cleanClockListBean.getTotalPage()) {
                    isRefresh = false;
                    getList();
                } else {
                    lvClean.setNoMore(true);
                }
            }
        });

        lvClean.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                getList();
            }
        });
        getList();
    }

    private void getList() {
        listMap.clear();
//        listMap.put("userId", SPUtil.get(this, AppConfig.id, ""));
//        listMap.put("username", SPUtil.get(this, AppConfig.name, ""));
        listMap.put("communityId", AppConfig.COMMUNITYID);
        listMap.put("taskType", 2);
        if (isRefresh)
            page = 1;
        else
            page++;
        listMap.put("page", page);
        listMap.put("size", AppConfig.pageSize);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getCleanClockList(listMap), new Observer<BaseEntity<CleanClockListBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<CleanClockListBean> cleanClockListBeanBaseEntity) {
                if (cleanClockListBeanBaseEntity.isSuccess()) {
                    lvClean.refreshComplete(AppConfig.pageSize);
                    cleanClockListBean = cleanClockListBeanBaseEntity.getData();
                    if (cleanClockListBean != null && cleanClockListBean.getRecords() != null) {
                        if (isRefresh) {
                            if (cleanClockListBean.getRecords().size() > 0) {
                                multipleStatusView.showContent();
                                adapter.setDataList(cleanClockListBean.getRecords());
                            } else {
                                multipleStatusView.showEmpty();
                            }
                        } else {
                            adapter.addAll(cleanClockListBean.getRecords());
                        }
                    } else {
                        multipleStatusView.showEmpty();
                    }
                } else {
                    multipleStatusView.showError();
                }
            }

            @Override
            public void onError(Throwable e) {
                multipleStatusView.showError();
                lvClean.refreshComplete(AppConfig.pageSize);
            }

            @Override
            public void onComplete() {
                lvClean.refreshComplete(AppConfig.pageSize);
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.btn_right_action_bar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_right_action_bar:
                PictureSelector.create(this)
                        .openCamera(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
//                     例如 LocalMedia 里面返回三种path
//                     1.media.getPath(); 为原图path
//                     2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
//                     3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
//                     如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (uploadInfo != null) {
                        uploadDialog.showLoading("上传图片中...");
                        uploadInfo.setBucket(AppConfig.BUCKET_NAME);
                        imgUrl = OssManager.getInstance().uploadFileToAliYun(uploadInfo, selectList.get(0).getPath(), new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest ossRequest, PutObjectResult ossResult) {
                                addClock();
                            }

                            @Override
                            public void onFailure(PutObjectRequest ossRequest, ClientException e, ServiceException e1) {
                                uploadDialog.dismiss();
                                ToastUtil.showShort("上传失败");
                            }
                        });
                    }
                    break;
            }
        }
    }

    private void addClock() {
        map.clear();
        map.put("userId", SPUtil.get(this, AppConfig.id, ""));
//        map.put("username", SPUtil.get(this, AppConfig.name, ""));
        map.put("communityId", AppConfig.COMMUNITYID);
        map.put("taskType", 2);
        map.put("url", imgUrl);
//        map.put("creatorId", SPUtil.get(this, AppConfig.id, ""));
//        map.put("createAt", TimeUtils.getCurrentTimeWithT());
//        map.put("dataStatus", "1");
        RetrofitManage.getInstance().subscribe(Api.getInstance().addClock(map), new Observer<BaseEntity<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<Object> stringBaseEntity) {
                if (stringBaseEntity.isSuccess()) {
                    uploadDialog.showSuccess("上传成功");
                    isRefresh = true;
                    getList();
                } else {
                    uploadDialog.showError(stringBaseEntity.getErrorMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                uploadDialog.showError(ThrowableUtils.getInstance(CleanClockListActivity.this).downloadException(e));
            }

            @Override
            public void onComplete() {
                uploadDialog.dismiss();
            }
        });
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
}
