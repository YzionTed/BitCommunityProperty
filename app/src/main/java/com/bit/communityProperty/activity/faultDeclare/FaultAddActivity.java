package com.bit.communityProperty.activity.faultDeclare;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.bean.HomeMenuBean;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.GlideUtils;
import com.bit.communityProperty.utils.OssManager;
import com.bit.communityProperty.utils.PicSelectUtils;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.view.TitleBarView;
import com.bit.communityProperty.widget.NoScrollGridView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private void initView(){
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("新增故障");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNoScrollGridView = findViewById(R.id.ns_grid);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        initGridView();
    }

    /**
     * 初始化网格布局：图片评论
     */
    private void initGridView() {
        mAdapter = new CommonAdapter<HomeMenuBean>(mContext, R.layout.picture_comment_item) {
            @Override
            public void convert(ViewHolder holder, HomeMenuBean homeMenuBean, final int position, View convertView) {
                if (homeMenuBean.getmImageID()!=0) {
                    holder.setImageResource(R.id.iv_icon, homeMenuBean.getmImageID());
                }else {
                    try {
                        holder.setImageResource(R.id.iv_icon, Uri.fromFile(new File(homeMenuBean.getmImageUrl())));
                    }catch (Exception e){

                    }
                }
                holder.setOnClickListener(R.id.grid_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position<4&&pictureNum==position+1) {
                            PicSelectUtils.startPhoto((Activity) mContext, 4-position, PictureConfig.MULTIPLE);
                        }
                    }
                });
                holder.setOnLongClickListener(R.id.grid_item, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return true;
                    }
                });
            }
        };
        mNoScrollGridView.setAdapter(mAdapter);
        addPicture = new HomeMenuBean();//暂时用这个bean类
        addPicture.setmImageID(R.mipmap.add);
        List<HomeMenuBean> mainWorkBeanList = new ArrayList<>();
        mainWorkBeanList.add(addPicture);
        pictureNum=1;
        mAdapter.setDatas(mainWorkBeanList);
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
                    for (int i= 0;i<selectList.size();i++){
                        HomeMenuBean bean = new HomeMenuBean();
                        String rulstr = selectList.get(i).getCompressPath();
                        bean.setmImageUrl(rulstr);
                        list.add(bean);

                    }
                    mAdapter.remove(pictureNum-1);
                    pictureNum = pictureNum+list.size();
                    if (pictureNum<4)
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
//                            public void onSuccess(PutObjectRequest ossRequest, PutObjectResult ossResult) {
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
