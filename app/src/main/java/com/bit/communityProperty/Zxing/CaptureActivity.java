package com.bit.communityProperty.Zxing;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.Zxing.camera.CameraManager;
import com.bit.communityProperty.Zxing.decoding.CaptureActivityHandler;
import com.bit.communityProperty.Zxing.decoding.InactivityTimer;
import com.bit.communityProperty.Zxing.view.ViewfinderView;
import com.bit.communityProperty.activity.releasePass.ReleasePassDetailsActivity;
import com.bit.communityProperty.activity.releasePass.bean.ReleasePassDetailsBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.PermissionHelper;
import com.bit.communityProperty.view.TitleBarView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CaptureActivity extends BaseActivity implements Callback, View.OnClickListener {

    public final static String TAG = "CaptureActivity";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private SharedPreferences mSF;
    private static final int VERSION = 3;

    private TextView qr_ex;
    private TextView textView2;

    private PermissionHelper mHelper;
    private TitleBarView mTitleBarView;//标题栏

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr;
    }

    @Override
    public void initViewData() {
        // 初始化 CameraManager
        CameraManager.init(getApplication());
        mSF = getSharedPreferences("remeber", MODE_PRIVATE);
        viewfinderView =findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        qr_ex = findViewById(R.id.qr_ex);
        qr_ex.setOnClickListener(this);
        textView2 = findViewById(R.id.textView2);
        mHelper = new PermissionHelper(this);
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("扫一扫");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //判断是否有相机权限
            if (cameraIsCanUse()) {
                initView();
            } else {
//                DialogUtils.getInstance().showNativeDialogSingleBtn(CaptureActivity.this, R.string.dialog_permission_camera_prompt, false);
            }
        } else {
            mHelper.requestPermissions("为保证该功能正常使用，请允许相机权限。", new PermissionHelper.PermissionListener() {
                @Override
                public void doAfterGrand(String... permission) {
                    hasSurface = true;
                }

                @Override
                public void doAfterDenied(String... permission) {
//                    DialogUtils.getInstance().showNativeDialogSingleBtn(CaptureActivity.this, R.string.dialog_permission_miss_prompt, false);
                }
            }, Manifest.permission.CAMERA);
        }

    }

    private void initView() {
        try {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            if (hasSurface) {
                initCamera(surfaceHolder);
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
            decodeFormats = null;
            characterSet = null;

            playBeep = true;
            AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
            if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
                playBeep = false;
            }
            initBeepSound();
            vibrate = true;
        } catch (Exception e) {
            Log.e(TAG, "initCamera Exception");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && hasSurface) {
            initView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
//            DialogUtils.getInstance().showNativeDialogSingleBtn(CaptureActivity.this, R.string.dialog_permission_camera_prompt, false);
            return;
        } catch (RuntimeException e) {
//            DialogUtils.getInstance().showNativeDialogSingleBtn(CaptureActivity.this, R.string.dialog_permission_camera_prompt, false);
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (null == holder) {
                Log.e(TAG, "** WARNING ** surfaceCreated is null.");
            }
            if (!hasSurface) {
                hasSurface = true;
                initCamera(holder);
            }
        } catch (Exception e) {
            Log.e(TAG, "surfaceCreated is Exception");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    // 处理返回的结果
    public void handleDecode(Result obj, Bitmap barcode) {
        String result = obj.toString().trim();
        LogManager.i(result);
        inactivityTimer.onActivity();
        viewfinderView.drawResultBitmap(barcode);
        playBeepSoundAndVibrate();
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(result)){
            map = getMapData(result);
            if (map!=null &&map.get("TYPE").equals("001")) {//扫码放行
                String data = map.get("DATA");
                if (!TextUtils.isEmpty(data)&&data.length()>8) {
                    String id = data.substring(8,data.length());
                    Intent intent = new Intent(this, ReleasePassDetailsActivity.class);
                    intent.putExtra("PASS_Id", id);
                    startActivity(intent);
                    finish();
                }else {
                    qrCodeErrorShow("二维码信息有误");
                }
            }else {
                String rul = result;
                Uri uri = Uri.parse(rul);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                CaptureActivity.this.finish();
                return;
            }
        }else {
            qrCodeErrorShow("二维码无法识别");
        }
    }


    /**
     * 处理二维码数据
     * @param str
     * @return
     */
    private Map<String, String> getMapData(String str) {
        //http://bit.cn/bit/1/1000/no/001/para/id/5a9809bf0f0c4547832c5107
        Map<String, String> map = new HashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        if (str.indexOf("/bit/") != -1) {
            str = str.substring(str.indexOf("/bit/"), str.length());
            String zs = str.substring(str.indexOf("/bit/"), str.length());
            int zs1 = zs.indexOf("/");//第一个位置
            while (zs1 != -1) {
                zs1 = zs.indexOf("/", zs1 + 1);
                list.add(zs1);
            }
            for (int i = 0; i < list.size(); i++) {
                switch (i) {
                    case 1:
                        map.put("VERSION", str.substring(list.get(0) + 1, list.get(i)));//版本号
                        break;
                    case 2:
                        map.put("CLIENT_TYPE", str.substring(list.get(1) + 1, list.get(i)));//客户端类型
                        break;
                    case 3:
                        map.put("IS_ENCRYPT", str.substring(list.get(2) + 1, list.get(i)));//是否加密
                        break;
                    case 4:
                        map.put("TYPE", str.substring(list.get(3) + 1, list.get(i)));//操作类型
                        break;
                    case 5:
                        map.put("DATA", str.substring(list.get(4) + 1, str.length()));//数据
                        break;
                }

            }
        }
        return map;
    }


    /**
     * 获取放行条详情
     * @param releaseId 放行条id=5a925f666951ff48ffd1c5cd
     */
    private  void getReleasePassInfo(String releaseId){
        String url = "/v1/property/rpass/"+releaseId+"/detail";
        RetrofitManage.getInstance().subscribe(Api.getInstance().getReleasePassInfo(url), new Observer<BaseEntity<ReleasePassDetailsBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<ReleasePassDetailsBean> BaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(BaseEntity));

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 二维码错误提示信息
     */
    private void qrCodeErrorShow() {
        setTextView("您的二维码信息错误");
    }

    private void qrCodeErrorShow(int stringResources) {
        setTextView(getApplication().getString(stringResources));
    }

    private void qrCodeErrorShow(String stringResources) {
        setTextView(stringResources);
    }

    private String clearSpace(String str) {

        StringBuilder sb = new StringBuilder();

        if (!TextUtils.isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (!Character.isSpaceChar(c)) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    private String getStrCode(String result, int qrVerName) {
        String resultToken = "";
        StringBuilder sb = new StringBuilder();
        String codes[] = result.split(",");
        switch (qrVerName) {
            case 4: // 第四版的二维码通讯协议
                for (int i = 2; i < codes.length; i++) {
                    sb.append(codes[i]);
                    sb.append(",");
                }
                resultToken = sb.deleteCharAt(sb.length() - 1).toString();
                break;
            case 5:// 第五版的二维码通讯协议
                for (int i = 3; i < codes.length; i++) {
                    sb.append(codes[i]);
                    sb.append(",");
                }
                resultToken = sb.deleteCharAt(sb.length() - 1).toString();
                break;
            case 6:// 第六版的二维码通讯协议
                for (int i = 0; i < codes.length; i++) {
                    sb.append(codes[i]);
                    sb.append(",");
                }
                resultToken = sb.deleteCharAt(sb.length() - 1).toString();
                break;
        }
        return resultToken;
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.qr_ex:// 退出扫描界面
                this.finish();
                break;
            default:
                break;
        }
    }


    private void setTextView(String text) {
        textView2.setText(text);
        textView2.setVisibility(View.VISIBLE);
    }

    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    private static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

}