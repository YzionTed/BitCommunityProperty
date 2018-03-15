package com.bit.communityProperty.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.listener.StandardVideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.shuyu.gsyvideoplayer.video.GSYBaseVideoPlayer;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import moe.codeest.enviews.ENDownloadView;
import moe.codeest.enviews.ENPlayView;

/**
 * Created by DELL60 on 2018/1/31.
 */

public class CustomVideo extends GSYVideoPlayer {


    protected Timer mDismissControlViewTimer;
    protected ProgressBar mBottomProgressBar;
    private View mLoadingProgressBar;
    protected TextView mTitleTextView;
    protected RelativeLayout mThumbImageViewLayout;
    private View mThumbImageView;
    protected Dialog mBrightnessDialog;
    protected TextView mBrightnessDialogTv;
    protected Dialog mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;
    protected StandardVideoAllCallBack mStandardVideoAllCallBack;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;
    protected LockClickListener mLockClickListener;
    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;
    protected ImageView mLockScreen;
    protected Drawable mBottomProgressDrawable;
    protected Drawable mBottomShowProgressDrawable;
    protected Drawable mBottomShowProgressThumbDrawable;
    protected Drawable mVolumeProgressDrawable;
    protected Drawable mDialogProgressBarDrawable;
    protected boolean mLockCurScreen;
    protected boolean mNeedLockFull;
    private boolean mThumbPlay;
    private int mDialogProgressHighLightColor = -11;
    private int mDialogProgressNormalColor = -11;

    private ImageView ivJieTu;

    public void setStandardVideoAllCallBack(StandardVideoAllCallBack standardVideoAllCallBack) {
        this.mStandardVideoAllCallBack = standardVideoAllCallBack;
        this.setVideoAllCallBack(standardVideoAllCallBack);
    }

    public CustomVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CustomVideo(Context context) {
        super(context);
    }

    public CustomVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    protected void init(Context context) {
        super.init(context);
        this.mBottomProgressBar = (ProgressBar)this.findViewById(R.id.bottom_progressbar);
        this.mTitleTextView = (TextView)this.findViewById(R.id.title);
        this.mThumbImageViewLayout = (RelativeLayout)this.findViewById(R.id.thumb);
        this.mLockScreen = (ImageView)this.findViewById(R.id.lock_screen);
        this.mLoadingProgressBar = this.findViewById(R.id.loading);
        this.mThumbImageViewLayout.setVisibility(GONE);
        this.mThumbImageViewLayout.setOnClickListener(this);
        this.mBackButton.setOnClickListener(this);
        if(this.mThumbImageView != null && !this.mIfCurrentIsFullscreen) {
            this.mThumbImageViewLayout.removeAllViews();
            this.resolveThumbImage(this.mThumbImageView);
        }

        if(this.mBottomProgressDrawable != null) {
            this.mBottomProgressBar.setProgressDrawable(this.mBottomProgressDrawable);
        }

        if(this.mBottomShowProgressDrawable != null) {
            this.mProgressBar.setProgressDrawable(this.mBottomProgressDrawable);
        }

        if(this.mBottomShowProgressThumbDrawable != null) {
            this.mProgressBar.setThumb(this.mBottomShowProgressThumbDrawable);
        }

        this.mLockScreen.setVisibility(GONE);
        this.mLockScreen.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(CustomVideo.this.mCurrentState != 6 && CustomVideo.this.mCurrentState != 7) {
                    CustomVideo.this.lockTouchLogic();
                    if(CustomVideo.this.mLockClickListener != null) {
                        CustomVideo.this.mLockClickListener.onClick(v, CustomVideo.this.mLockCurScreen);
                    }
                }
            }
        });

        ivJieTu = findViewById(R.id.iv_pz);
    }

    public boolean setUp(String url, boolean cacheWithPlay, Object... objects) {
        return this.setUp(url, cacheWithPlay, (File)null, objects);
    }

    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Object... objects) {
        if(super.setUp(url, cacheWithPlay, cachePath, objects)) {
            if(objects != null && objects.length > 0) {
                this.mTitleTextView.setText(objects[0].toString());
            }

            if(this.mIfCurrentIsFullscreen) {
                this.mFullscreenButton.setImageResource(this.getShrinkImageRes());
            } else {
                this.mFullscreenButton.setImageResource(this.getEnlargeImageRes());
                this.mBackButton.setVisibility(GONE);
            }

            return true;
        } else {
            return false;
        }
    }

    //获取播放视图view
    public TextureView getTextureView(){
        return this.mTextureView;
    }

    //获取截图按钮
    public ImageView getScreenShot(){
        return ivJieTu;
    }

    public TextView getCurrTextView(){
        return this.mCurrentTimeTextView;
    }

    public SeekBar getSeekBar(){
        return this.mProgressBar;
    }

    public TextView getTotalTextView(){
        return this.mTotalTimeTextView;
    }

    public int getLayoutId() {
        return R.layout.layout_custom_video;
    }

    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch(this.mCurrentState) {
            case 0:
                this.changeUiToNormal();
                this.cancelDismissControlViewTimer();
                break;
            case 1:
                this.changeUiToPrepareingShow();
//                this.startDismissControlViewTimer();
                break;
            case 2:
                this.changeUiToPlayingShow();
//                this.startDismissControlViewTimer();
                break;
            case 3:
                this.changeUiToPlayingBufferingShow();
            case 4:
            default:
                break;
            case 5:
                this.changeUiToPauseShow();
                this.cancelDismissControlViewTimer();
                break;
            case 6:
                this.changeUiToCompleteShow();
                this.cancelDismissControlViewTimer();
                this.mBottomProgressBar.setProgress(100);
                break;
            case 7:
                this.changeUiToError();
        }

    }

//    public boolean onTouch(View v, MotionEvent event) {
//        int id = v.getId();
//        if(id == id.surface_container) {
//            switch(event.getAction()) {
//                case 0:
//                case 2:
//                default:
//                    break;
//                case 1:
//                    this.startDismissControlViewTimer();
//                    if(this.mChangePosition) {
//                        int duration = this.getDuration();
//                        int progress = this.mSeekTimePosition * 100 / (duration == 0?1:duration);
//                        this.mBottomProgressBar.setProgress(progress);
//                    }
//
//                    if(!this.mChangePosition && !this.mChangeVolume && !this.mBrightness) {
//                        this.onClickUiToggle();
//                    }
//            }
//        } else if(id == id.progress) {
//            switch(event.getAction()) {
//                case 0:
//                    this.cancelDismissControlViewTimer();
//                    break;
//                case 1:
//                    this.startDismissControlViewTimer();
//            }
//        }
//
//        return this.mIfCurrentIsFullscreen && this.mLockCurScreen && this.mNeedLockFull?true:super.onTouch(v, event);
//    }

    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if(i == R.id.thumb) {
            if(!this.mThumbPlay) {
                return;
            }

            if(TextUtils.isEmpty(this.mUrl)) {
                Toast.makeText(this.getContext(), this.getResources().getString(R.string.no_url), Toast.LENGTH_LONG).show();
                return;
            }

            if(this.mCurrentState == 0) {
                if(!this.mUrl.startsWith("file") && !CommonUtil.isWifiConnected(this.getContext()) && this.mNeedShowWifiTip) {
                    this.showWifiDialog();
                    return;
                }

                this.startPlayLogic();
            } else if(this.mCurrentState == 6) {
                this.onClickUiToggle();
            }
        } else if(i == R.id.surface_container) {
            if(this.mStandardVideoAllCallBack != null && this.isCurrentMediaListener()) {
                if(this.mIfCurrentIsFullscreen) {
                    Debuger.printfLog("onClickBlankFullscreen");
                    this.mStandardVideoAllCallBack.onClickBlankFullscreen(this.mUrl, this.mObjects);
                } else {
                    Debuger.printfLog("onClickBlank");
                    this.mStandardVideoAllCallBack.onClickBlank(this.mUrl, this.mObjects);
                }
            }

//            this.startDismissControlViewTimer();
        }

    }

    public void showWifiDialog() {
        super.showWifiDialog();
        if(!NetworkUtils.isAvailable(this.mContext)) {
            Toast.makeText(this.mContext, this.getResources().getString(R.string.no_net), Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setMessage(this.getResources().getString(R.string.tips_not_wifi));
            builder.setPositiveButton(this.getResources().getString(R.string.tips_not_wifi_confirm), new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CustomVideo.this.startPlayLogic();
                }
            });
            builder.setNegativeButton(this.getResources().getString(R.string.tips_not_wifi_cancel), new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    public void startPlayLogic() {
        if(this.mStandardVideoAllCallBack != null) {
            Debuger.printfLog("onClickStartThumb");
            this.mStandardVideoAllCallBack.onClickStartThumb(this.mUrl, this.mObjects);
        }

        this.prepareVideo();
//        this.startDismissControlViewTimer();
    }

    protected void onClickUiToggle() {
        if(this.mIfCurrentIsFullscreen && this.mLockCurScreen && this.mNeedLockFull) {
            this.mLockScreen.setVisibility(VISIBLE);
        } else {
            if(this.mCurrentState == 1) {
                if(this.mBottomContainer.getVisibility() == VISIBLE) {
                    this.changeUiToPrepareingClear();
                } else {
                    this.changeUiToPrepareingShow();
                }
            } else if(this.mCurrentState == 2) {
                if(this.mBottomContainer.getVisibility() == VISIBLE) {
                    this.changeUiToPlayingClear();
                } else {
                    this.changeUiToPlayingShow();
                }
            } else if(this.mCurrentState == 5) {
                if(this.mBottomContainer.getVisibility() == VISIBLE) {
                    this.changeUiToPauseClear();
                } else {
                    this.changeUiToPauseShow();
                }
            } else if(this.mCurrentState == 6) {
                if(this.mBottomContainer.getVisibility() == VISIBLE) {
                    this.changeUiToCompleteClear();
                } else {
                    this.changeUiToCompleteShow();
                }
            } else if(this.mCurrentState == 3) {
                if(this.mBottomContainer.getVisibility() == VISIBLE) {
                    this.changeUiToPlayingBufferingClear();
                } else {
                    this.changeUiToPlayingBufferingShow();
                }
            }

        }
    }

    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
        if(progress != 0) {
            this.mBottomProgressBar.setProgress(progress);
        }

        if(secProgress != 0 && !this.mCacheFile) {
            this.mBottomProgressBar.setSecondaryProgress(secProgress);
        }

    }

    protected void resetProgressAndTime() {
        super.resetProgressAndTime();
        this.mBottomProgressBar.setProgress(0);
        this.mBottomProgressBar.setSecondaryProgress(0);
    }

    private void changeUiToNormal() {
        Debuger.printfLog("changeUiToNormal");
        this.mTopContainer.setVisibility(VISIBLE);
        this.mBottomContainer.setVisibility(INVISIBLE);
//        this.mStartButton.setVisibility(GONE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(VISIBLE);
        this.mCoverImageView.setVisibility(VISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(this.mIfCurrentIsFullscreen && this.mNeedLockFull?VISIBLE:GONE);
        this.updateStartImage();
    }

    private void changeUiToPrepareingShow() {
        Debuger.printfLog("changeUiToPrepareingShow");
        this.mTopContainer.setVisibility(VISIBLE);
        this.mBottomContainer.setVisibility(VISIBLE);
//        this.mStartButton.setVisibility(INVISIBLE);
        this.mLoadingProgressBar.setVisibility(VISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView)this.mLoadingProgressBar;
            if(enDownloadView.getCurrentState() == 0) {
                ((ENDownloadView)this.mLoadingProgressBar).start();
            }
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mCoverImageView.setVisibility(VISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(GONE);
    }

    private void changeUiToPrepareingClear() {
        Debuger.printfLog("changeUiToPrepareingClear");
        this.mTopContainer.setVisibility(INVISIBLE);
        this.mBottomContainer.setVisibility(INVISIBLE);
//        this.mStartButton.setVisibility(INVISIBLE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mCoverImageView.setVisibility(VISIBLE);
        this.mLockScreen.setVisibility(GONE);
    }

    private void changeUiToPlayingShow() {
        Debuger.printfLog("changeUiToPlayingShow");
        this.mTopContainer.setVisibility(VISIBLE);
        this.mBottomContainer.setVisibility(VISIBLE);
//        this.mStartButton.setVisibility(GONE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mCoverImageView.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(this.mIfCurrentIsFullscreen && this.mNeedLockFull?VISIBLE:GONE);
        this.updateStartImage();
    }

    private void changeUiToPlayingClear() {
        Debuger.printfLog("changeUiToPlayingClear");
        this.changeUiToClear();
        this.mBottomProgressBar.setVisibility(VISIBLE);
    }

    private void changeUiToPauseShow() {
        Debuger.printfLog("changeUiToPauseShow");
        this.mTopContainer.setVisibility(VISIBLE);
        this.mBottomContainer.setVisibility(VISIBLE);
//        this.mStartButton.setVisibility(GONE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(this.mIfCurrentIsFullscreen && this.mNeedLockFull?VISIBLE:GONE);
        this.updateStartImage();
        this.updatePauseCover();
    }

    private void changeUiToPauseClear() {
        Debuger.printfLog("changeUiToPauseClear");
        this.changeUiToClear();
        this.mBottomProgressBar.setVisibility(VISIBLE);
        this.updatePauseCover();
    }

    private void changeUiToPlayingBufferingShow() {
        Debuger.printfLog("changeUiToPlayingBufferingShow");
        this.mTopContainer.setVisibility(VISIBLE);
        this.mBottomContainer.setVisibility(VISIBLE);
//        this.mStartButton.setVisibility(INVISIBLE);
        this.mLoadingProgressBar.setVisibility(VISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView)this.mLoadingProgressBar;
            if(enDownloadView.getCurrentState() == 0) {
                ((ENDownloadView)this.mLoadingProgressBar).start();
            }
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mCoverImageView.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(INVISIBLE);
    }

    private void changeUiToPlayingBufferingClear() {
        Debuger.printfLog("changeUiToPlayingBufferingClear");
        this.mTopContainer.setVisibility(INVISIBLE);
        this.mBottomContainer.setVisibility(INVISIBLE);
//        this.mStartButton.setVisibility(INVISIBLE);
        this.mLoadingProgressBar.setVisibility(VISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ENDownloadView enDownloadView = (ENDownloadView)this.mLoadingProgressBar;
            if(enDownloadView.getCurrentState() == 0) {
                ((ENDownloadView)this.mLoadingProgressBar).start();
            }
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mCoverImageView.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(VISIBLE);
        this.mLockScreen.setVisibility(GONE);
        this.updateStartImage();
    }

    private void changeUiToClear() {
        Debuger.printfLog("changeUiToClear");
        this.mTopContainer.setVisibility(INVISIBLE);
        this.mBottomContainer.setVisibility(INVISIBLE);
//        this.mStartButton.setVisibility(INVISIBLE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mCoverImageView.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(GONE);
    }

    private void changeUiToCompleteShow() {
        Debuger.printfLog("changeUiToCompleteShow");
        this.mTopContainer.setVisibility(VISIBLE);
        this.mBottomContainer.setVisibility(VISIBLE);
//        this.mStartButton.setVisibility(GONE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(VISIBLE);
        this.mCoverImageView.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(this.mIfCurrentIsFullscreen && this.mNeedLockFull?VISIBLE:GONE);
        this.updateStartImage();
    }

    private void changeUiToCompleteClear() {
        Debuger.printfLog("changeUiToCompleteClear");
        this.mTopContainer.setVisibility(INVISIBLE);
        this.mBottomContainer.setVisibility(INVISIBLE);
//        this.mStartButton.setVisibility(GONE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(VISIBLE);
        this.mCoverImageView.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(VISIBLE);
        this.mLockScreen.setVisibility(this.mIfCurrentIsFullscreen && this.mNeedLockFull?VISIBLE:GONE);
        this.updateStartImage();
    }

    private void changeUiToError() {
        Debuger.printfLog("changeUiToError");
        this.mTopContainer.setVisibility(INVISIBLE);
        this.mBottomContainer.setVisibility(INVISIBLE);
//        this.mStartButton.setVisibility(GONE);
        this.mLoadingProgressBar.setVisibility(INVISIBLE);
        if(this.mLoadingProgressBar instanceof ENDownloadView) {
            ((ENDownloadView)this.mLoadingProgressBar).reset();
        }

        this.mThumbImageViewLayout.setVisibility(INVISIBLE);
        this.mCoverImageView.setVisibility(VISIBLE);
        this.mBottomProgressBar.setVisibility(INVISIBLE);
        this.mLockScreen.setVisibility(this.mIfCurrentIsFullscreen && this.mNeedLockFull?VISIBLE:GONE);
        this.updateStartImage();
    }

    protected void updateStartImage() {
        ENPlayView enPlayView = (ENPlayView)this.mStartButton;
        enPlayView.setDuration(500);
        if(this.mCurrentState == 2) {
            enPlayView.play();
        } else if(this.mCurrentState == 7) {
            enPlayView.pause();
        } else {
            enPlayView.pause();
        }

    }

    private void updatePauseCover() {
        if((this.mFullPauseBitmap == null || this.mFullPauseBitmap.isRecycled()) && this.mShowPauseCover) {
            try {
                this.mFullPauseBitmap = this.mTextureView.getBitmap(this.mTextureView.getSizeW(), this.mTextureView.getSizeH());
            } catch (Exception var2) {
                var2.printStackTrace();
                this.mFullPauseBitmap = null;
            }
        }

        this.showPauseCover();
    }

    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if(this.mProgressDialog == null) {
            View localView = LayoutInflater.from(this.getContext()).inflate(R.layout.video_progress_dialog, (ViewGroup)null);
            this.mDialogProgressBar = (ProgressBar)localView.findViewById(R.id.duration_progressbar);
            if(this.mDialogProgressBarDrawable != null) {
                this.mDialogProgressBar.setProgressDrawable(this.mDialogProgressBarDrawable);
            }

            this.mDialogSeekTime = (TextView)localView.findViewById(R.id.tv_current);
            this.mDialogTotalTime = (TextView)localView.findViewById(R.id.tv_duration);
            this.mDialogIcon = (ImageView)localView.findViewById(R.id.duration_image_tip);
            this.mProgressDialog = new Dialog(this.getContext(), R.style.video_style_dialog_progress);
            this.mProgressDialog.setContentView(localView);
            this.mProgressDialog.getWindow().addFlags(8);
            this.mProgressDialog.getWindow().addFlags(32);
            this.mProgressDialog.getWindow().addFlags(16);
            this.mProgressDialog.getWindow().setLayout(this.getWidth(), this.getHeight());
            if(this.mDialogProgressNormalColor != -11) {
                this.mDialogTotalTime.setTextColor(this.mDialogProgressNormalColor);
            }

            if(this.mDialogProgressHighLightColor != -11) {
                this.mDialogSeekTime.setTextColor(this.mDialogProgressHighLightColor);
            }

            WindowManager.LayoutParams localLayoutParams = this.mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 48;
            localLayoutParams.width = this.getWidth();
            localLayoutParams.height = this.getHeight();
            int[] location = new int[2];
            this.getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            this.mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }

        if(!this.mProgressDialog.isShowing()) {
            this.mProgressDialog.show();
        }

        this.mDialogSeekTime.setText(seekTime);
        this.mDialogTotalTime.setText(" / " + totalTime);
        if(totalTimeDuration > 0) {
            this.mDialogProgressBar.setProgress(seekTimePosition * 100 / totalTimeDuration);
        }

        if(deltaX > 0.0F) {
            this.mDialogIcon.setBackgroundResource(R.drawable.video_forward_icon);
        } else {
            this.mDialogIcon.setBackgroundResource(R.drawable.video_backward_icon);
        }

    }

    protected void dismissProgressDialog() {
        super.dismissProgressDialog();
        if(this.mProgressDialog != null) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }

    }

    protected void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if(this.mVolumeDialog == null) {
            View localView = LayoutInflater.from(this.getContext()).inflate(R.layout.video_volume_dialog, (ViewGroup)null);
            this.mDialogVolumeProgressBar = (ProgressBar)localView.findViewById(R.id.volume_progressbar);
            if(this.mVolumeProgressDrawable != null) {
                this.mDialogVolumeProgressBar.setProgressDrawable(this.mVolumeProgressDrawable);
            }

            this.mVolumeDialog = new Dialog(this.getContext(), R.style.video_style_dialog_progress);
            this.mVolumeDialog.setContentView(localView);
            this.mVolumeDialog.getWindow().addFlags(8);
            this.mVolumeDialog.getWindow().addFlags(32);
            this.mVolumeDialog.getWindow().addFlags(16);
            this.mVolumeDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = this.mVolumeDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 51;
            localLayoutParams.width = this.getWidth();
            localLayoutParams.height = this.getHeight();
            int[] location = new int[2];
            this.getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            this.mVolumeDialog.getWindow().setAttributes(localLayoutParams);
        }

        if(!this.mVolumeDialog.isShowing()) {
            this.mVolumeDialog.show();
        }

        this.mDialogVolumeProgressBar.setProgress(volumePercent);
    }

    protected void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if(this.mVolumeDialog != null) {
            this.mVolumeDialog.dismiss();
            this.mVolumeDialog = null;
        }

    }

    protected void showBrightnessDialog(float percent) {
        if(this.mBrightnessDialog == null) {
            View localView = LayoutInflater.from(this.getContext()).inflate(R.layout.video_brightness, (ViewGroup)null);
            this.mBrightnessDialogTv = (TextView)localView.findViewById(R.id.app_video_brightness);
            this.mBrightnessDialog = new Dialog(this.getContext(), R.style.video_style_dialog_progress);
            this.mBrightnessDialog.setContentView(localView);
            this.mBrightnessDialog.getWindow().addFlags(8);
            this.mBrightnessDialog.getWindow().addFlags(32);
            this.mBrightnessDialog.getWindow().addFlags(16);
            this.mBrightnessDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = this.mBrightnessDialog.getWindow().getAttributes();
            localLayoutParams.gravity = 53;
            localLayoutParams.width = this.getWidth();
            localLayoutParams.height = this.getHeight();
            int[] location = new int[2];
            this.getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            this.mBrightnessDialog.getWindow().setAttributes(localLayoutParams);
        }

        if(!this.mBrightnessDialog.isShowing()) {
            this.mBrightnessDialog.show();
        }

        if(this.mBrightnessDialogTv != null) {
            this.mBrightnessDialogTv.setText((int)(percent * 100.0F) + "%");
        }

    }

    protected void dismissBrightnessDialog() {
        super.dismissVolumeDialog();
        if(this.mBrightnessDialog != null) {
            this.mBrightnessDialog.dismiss();
            this.mBrightnessDialog = null;
        }

    }

    protected void loopSetProgressAndTime() {
        super.loopSetProgressAndTime();
        this.mBottomProgressBar.setProgress(0);
    }

    public void onBackFullscreen() {
        this.clearFullscreenLayout();
    }

    public void onAutoCompletion() {
        super.onAutoCompletion();
        if(this.mLockCurScreen) {
            this.lockTouchLogic();
            this.mLockScreen.setVisibility(GONE);
        }

    }

    public void onError(int what, int extra) {
        super.onError(what, extra);
        if(this.mLockCurScreen) {
            this.lockTouchLogic();
            this.mLockScreen.setVisibility(GONE);
        }

    }

    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        if(gsyBaseVideoPlayer != null) {
            CustomVideo gsyVideoPlayer = (CustomVideo)gsyBaseVideoPlayer;
            gsyVideoPlayer.setStandardVideoAllCallBack(this.mStandardVideoAllCallBack);
            gsyVideoPlayer.setLockClickListener(this.mLockClickListener);
            gsyVideoPlayer.setNeedLockFull(this.isNeedLockFull());
            this.initFullUI(gsyVideoPlayer);
        }

        return gsyBaseVideoPlayer;
    }

    public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.showSmallVideo(size, actionBar, statusBar);
        if(gsyBaseVideoPlayer != null) {
            CustomVideo gsyVideoPlayer = (CustomVideo)gsyBaseVideoPlayer;
            gsyVideoPlayer.setIsTouchWiget(false);
            gsyVideoPlayer.setStandardVideoAllCallBack(this.mStandardVideoAllCallBack);
        }

        return gsyBaseVideoPlayer;
    }

    protected void setSmallVideoTextureView(OnTouchListener onTouchListener) {
        super.setSmallVideoTextureView(onTouchListener);
        this.mThumbImageViewLayout.setOnTouchListener(onTouchListener);
    }

    private void lockTouchLogic() {
        if(this.mLockCurScreen) {
            this.mLockScreen.setImageResource(R.drawable.unlock);
            this.mLockCurScreen = false;
            if(this.mOrientationUtils != null) {
                this.mOrientationUtils.setEnable(this.mRotateViewAuto);
            }
        } else {
            this.mLockScreen.setImageResource(R.drawable.lock);
            this.mLockCurScreen = true;
            if(this.mOrientationUtils != null) {
                this.mOrientationUtils.setEnable(false);
            }

            this.hideAllWidget();
        }

    }

    public void initUIState() {
        this.setStateAndUi(0);
    }

    private void initFullUI(CustomVideo standardGSYVideoPlayer) {
        if(this.mBottomProgressDrawable != null) {
            standardGSYVideoPlayer.setBottomProgressBarDrawable(this.mBottomProgressDrawable);
        }

        if(this.mBottomShowProgressDrawable != null && this.mBottomShowProgressThumbDrawable != null) {
            standardGSYVideoPlayer.setBottomShowProgressBarDrawable(this.mBottomShowProgressDrawable, this.mBottomShowProgressThumbDrawable);
        }

        if(this.mVolumeProgressDrawable != null) {
            standardGSYVideoPlayer.setDialogVolumeProgressBar(this.mVolumeProgressDrawable);
        }

        if(this.mDialogProgressBarDrawable != null) {
            standardGSYVideoPlayer.setDialogProgressBar(this.mDialogProgressBarDrawable);
        }

        if(this.mDialogProgressHighLightColor >= 0 && this.mDialogProgressNormalColor >= 0) {
            standardGSYVideoPlayer.setDialogProgressColor(this.mDialogProgressHighLightColor, this.mDialogProgressNormalColor);
        }

    }

    private void startDismissControlViewTimer() {
        this.cancelDismissControlViewTimer();
        this.mDismissControlViewTimer = new Timer();
        this.mDismissControlViewTimerTask = new CustomVideo.DismissControlViewTimerTask();
        this.mDismissControlViewTimer.schedule(this.mDismissControlViewTimerTask, 2500L);
    }

    private void cancelDismissControlViewTimer() {
        if(this.mDismissControlViewTimer != null) {
            this.mDismissControlViewTimer.cancel();
            this.mDismissControlViewTimer = null;
        }

        if(this.mDismissControlViewTimerTask != null) {
            this.mDismissControlViewTimerTask.cancel();
            this.mDismissControlViewTimerTask = null;
        }

    }

    protected void hideAllWidget() {
        this.mBottomContainer.setVisibility(INVISIBLE);
        this.mTopContainer.setVisibility(INVISIBLE);
        this.mBottomProgressBar.setVisibility(VISIBLE);
//        this.mStartButton.setVisibility(INVISIBLE);
    }

    private void resolveThumbImage(View thumb) {
        this.mThumbImageViewLayout.addView(thumb);
        android.view.ViewGroup.LayoutParams layoutParams = thumb.getLayoutParams();
        layoutParams.height = -1;
        layoutParams.width = -1;
        thumb.setLayoutParams(layoutParams);
    }

    public void setThumbImageView(View view) {
        if(this.mThumbImageViewLayout != null) {
            this.mThumbImageView = view;
            this.resolveThumbImage(view);
        }

    }

    public void clearThumbImageView() {
        if(this.mThumbImageViewLayout != null) {
            this.mThumbImageViewLayout.removeAllViews();
        }

    }

    public TextView getTitleTextView() {
        return this.mTitleTextView;
    }

    public void setBottomShowProgressBarDrawable(Drawable drawable, Drawable thumb) {
        this.mBottomShowProgressDrawable = drawable;
        this.mBottomShowProgressThumbDrawable = thumb;
        if(this.mProgressBar != null) {
            this.mProgressBar.setProgressDrawable(drawable);
            this.mProgressBar.setThumb(thumb);
        }

    }

    public void setBottomProgressBarDrawable(Drawable drawable) {
        this.mBottomProgressDrawable = drawable;
        if(this.mBottomProgressBar != null) {
            this.mBottomProgressBar.setProgressDrawable(drawable);
        }

    }

    public void setDialogVolumeProgressBar(Drawable drawable) {
        this.mVolumeProgressDrawable = drawable;
    }

    public void setDialogProgressBar(Drawable drawable) {
        this.mDialogProgressBarDrawable = drawable;
    }

    public void setDialogProgressColor(int highLightColor, int normalColor) {
        this.mDialogProgressHighLightColor = highLightColor;
        this.mDialogProgressNormalColor = normalColor;
    }

    public void setThumbPlay(boolean thumbPlay) {
        this.mThumbPlay = thumbPlay;
    }

    public RelativeLayout getThumbImageViewLayout() {
        return this.mThumbImageViewLayout;
    }

    public boolean isNeedLockFull() {
        return this.mNeedLockFull;
    }

    public void setNeedLockFull(boolean needLoadFull) {
        this.mNeedLockFull = needLoadFull;
    }

    public void setLockClickListener(LockClickListener lockClickListener) {
        this.mLockClickListener = lockClickListener;
    }

    protected class DismissControlViewTimerTask extends TimerTask {
        protected DismissControlViewTimerTask() {
        }

        public void run() {
            if(CustomVideo.this.mCurrentState != 0 && CustomVideo.this.mCurrentState != 7 && CustomVideo.this.mCurrentState != 6 && CustomVideo.this.getContext() != null && CustomVideo.this.getContext() instanceof Activity) {
                ((Activity)CustomVideo.this.getContext()).runOnUiThread(new Runnable() {
                    public void run() {
                        CustomVideo.this.hideAllWidget();
                        CustomVideo.this.mLockScreen.setVisibility(GONE);
                        if(CustomVideo.this.mHideKey && CustomVideo.this.mIfCurrentIsFullscreen && CustomVideo.this.mShowVKey) {
                            CommonUtil.hideNavKey(CustomVideo.this.mContext);
                        }

                    }
                });
            }

        }
    }


}
