package com.bit.communityProperty.activity.videomonitor;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.utils.CustomVideo;
import com.bit.communityProperty.utils.DialogUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.Debuger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class HistoryMonitorActivity extends BaseActivity {

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
    @BindView(R.id.gsy_player)
    CustomVideo gsyVideoPlayer;
    @BindView(R.id.ll_lx)
    LinearLayout llLx;
    @BindView(R.id.tv_xinghao)
    TextView tvXinghao;

    private String url = "http://mvvideo1.meitudata.com/59100328a63116034.mp4";
    private MediaMetadataRetriever mCoverMedia;
    private ImageView imageView;
    private boolean isRelease;
    @Override
    public int getLayoutId() {
        return R.layout.activity_history_monitor;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("历史录像");
        setVideo(url);
        gsyVideoPlayer.startPlayLogic();
    }

    private void setVideo(String url) {
        //默认缓存路径
        gsyVideoPlayer.setUp(url, true, null, "");
        gsyVideoPlayer.setIsTouchWiget(false);//是否可拖动
        gsyVideoPlayer.setIsTouchWigetFull(false);//全屏是否可拖动
        gsyVideoPlayer.setBottomProgressBarDrawable(null);
        gsyVideoPlayer.setDialogVolumeProgressBar(null);
        gsyVideoPlayer.setShrinkImageRes(R.mipmap.ic_quanpin_shou);
        gsyVideoPlayer.setEnlargeImageRes(R.mipmap.ic_quanpin);
        gsyVideoPlayer.getFullscreenButton().setImageResource(R.mipmap.ic_quanpin);
        //holder.gsyVideoPlayer.setNeedShowWifiTip(false);

        /************************下方为其他路径************************************/
        //如果一个列表的缓存路劲都一一致
        //holder.gsyVideoPlayer.setUp(url, true, new File(FileUtils.getTestPath(), ""));

        /************************下方为其他路径************************************/
        //如果一个列表里的缓存路劲不一致
        //int playPosition = GSYVideoManager.instance().getPlayPosition();
        //避免全屏返回的时候不可用了
        /*if (playPosition < 0 || playPosition != position ||
                !GSYVideoManager.instance().getPlayTag().equals(ListNormalAdapter.TAG)) {
            holder.gsyVideoPlayer.initUIState();
        }*/
        //如果设置了点击封面可以播放，如果缓存列表路径不一致，还需要设置封面点击
        /*holder.gsyVideoPlayer.setThumbPlay(true);

        holder.gsyVideoPlayer.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要切换缓存路径的
                holder.gsyVideoPlayer.setUp(url, true, new File(FileUtils.getTestPath(), ""));
                holder.gsyVideoPlayer.startPlayLogic();
            }
        });

        holder.gsyVideoPlayer.getThumbImageViewLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要切换缓存路径的
                holder.gsyVideoPlayer.setUp(url, true, new File(FileUtils.getTestPath(), ""));
                holder.gsyVideoPlayer.startPlayLogic();
            }
        });*/

        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        gsyVideoPlayer.getCurrTextView().setVisibility(View.VISIBLE);
        gsyVideoPlayer.getSeekBar().setVisibility(View.VISIBLE);
        gsyVideoPlayer.getTotalTextView().setVisibility(View.VISIBLE);
        gsyVideoPlayer.getStartButton().setVisibility(View.VISIBLE);

        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });
        gsyVideoPlayer.getScreenShot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScreenShotBitmap(gsyVideoPlayer.getTextureView());
            }
        });
        gsyVideoPlayer.setRotateViewAuto(true);
        gsyVideoPlayer.setLockLand(false);
        gsyVideoPlayer.setShowFullAnimation(true);
        //循环
        //holder.gsyVideoPlayer.setLooping(true);
        gsyVideoPlayer.setNeedLockFull(false);
        imageView = new ImageView(this);
        //设置封面
        gsyVideoPlayer.setThumbImageView(imageView);
        //holder.gsyVideoPlayer.setSpeed(2);

        gsyVideoPlayer.setPlayPosition(0);
        gsyVideoPlayer.setStandardVideoAllCallBack(new StandardVideoAllCallBack() {
            @Override
            public void onClickStartThumb(String s, Object... objects) {

            }

            @Override
            public void onClickBlank(String s, Object... objects) {

            }

            @Override
            public void onClickBlankFullscreen(String s, Object... objects) {

            }

            @Override
            public void onPrepared(String s, Object... objects) {
                Debuger.printfLog("onPrepared");
                if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setNeedMute(false);
                }
            }

            @Override
            public void onClickStartIcon(String s, Object... objects) {

            }

            @Override
            public void onClickStartError(String s, Object... objects) {

            }

            @Override
            public void onClickStop(String s, Object... objects) {

            }

            @Override
            public void onClickStopFullscreen(String s, Object... objects) {

            }

            @Override
            public void onClickResume(String s, Object... objects) {

            }

            @Override
            public void onClickResumeFullscreen(String s, Object... objects) {

            }

            @Override
            public void onClickSeekbar(String s, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String s, Object... objects) {

            }

            @Override
            public void onAutoComplete(String s, Object... objects) {
            }

            @Override
            public void onEnterFullscreen(String s, Object... objects) {
                GSYVideoManager.instance().setNeedMute(false);
            }

            @Override
            public void onQuitFullscreen(String s, Object... objects) {
                GSYVideoManager.instance().setNeedMute(false);
            }

            @Override
            public void onQuitSmallWidget(String s, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String s, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String s, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String s, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String s, Object... objects) {

            }

            @Override
            public void onPlayError(String s, Object... objects) {

            }
        });
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final CustomVideo standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(this, false, true);
    }

    @Override
    public void onBackPressed() {
        if (CustomVideo.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRelease = true;
        GSYVideoPlayer.releaseAllVideos();
        if (mCoverMedia != null) {
            mCoverMedia.release();
            mCoverMedia = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    private void getScreenShotBitmap(TextureView vv) {
        String mPath = Environment.getExternalStorageDirectory().toString()
                + "/Pictures/" +System.currentTimeMillis()+ ".png";
        Bitmap bm = vv.getBitmap();
//        ivImg.setImageBitmap(bm);
        OutputStream fout = null;
        File imageFile = new File(mPath);
        try {
            fout = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bm!=null){
            DialogUtil.showConfirmDialog(this, "拍摄成功", "拍摄文件已保存到手机本地!", false, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.dissmiss();
                }
            });
        }
    }
}
