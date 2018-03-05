package com.bit.communityProperty.activity.access.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.Bluetooth.yunduijiang.YunDuiJIangUtils;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.access.DoorVideoViewActivity;
import com.bit.communityProperty.activity.access.VideoActiveOpenActivity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.PermissionUtils;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.widget.NoScrollGridView;
import com.ddclient.configuration.DongConfiguration;
import com.ddclient.dongsdk.DeviceInfo;
import com.ddclient.jnisdk.InfoUser;
import com.ddclient.push.DongPushMsgManager;
import com.gViewerX.util.LogUtils;
import com.smarthome.yunintercom.sdk.AbstractIntercomCallbackProxy;
import com.smarthome.yunintercom.sdk.IntercomSDK;
import com.smarthome.yunintercom.sdk.IntercomSDKProxy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class RemoteDoorFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    Unbinder unbinder;
    @BindView(R.id.ns_grid)
    NoScrollGridView nsGrid;
    private ArrayList<DeviceInfo> dataList = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private YunDuiJIangUtils yunDuiJIangUtils;

    private int clickPositonItem = -1;

    private String[] locPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//写入权限
            Manifest.permission.CAMERA,//相机
            Manifest.permission.CALL_PHONE,//电话
            Manifest.permission.RECORD_AUDIO//录音
    };
    private static final int REQUEST_CODE_LOCATION = 6;

    public static RemoteDoorFragment newInstance(int sectionNumber) {
        RemoteDoorFragment fragment = new RemoteDoorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_remote_door;
    }

    @Override
    protected void initViewAndData() {

        commonAdapter = new CommonAdapter<DeviceInfo>(mActivity, R.layout.door_control_adapter_item) {
            @Override
            public void convert(ViewHolder holder, DeviceInfo deviceInfo, int position, View convertView) {
                ((holder.<TextView>getView(R.id.tv_text))).setText(deviceInfo.deviceName);
            }
        };
        nsGrid.setAdapter(commonAdapter);
        commonAdapter.setDatas(dataList);
        nsGrid.setOnItemClickListener(this);

        yunDuiJIangUtils = new YunDuiJIangUtils(getActivity());
        yunDuiJIangUtils.login("13500000000", "123456");
        yunDuiJIangUtils.setOnYunDuiJIangListener(new YunDuiJIangUtils.OnYunDuiJIangListener() {
            @Override
            public void onNewListInfo(ArrayList<DeviceInfo> deviceInfoList) {
                RemoteDoorFragment.this.dataList = deviceInfoList;
                commonAdapter.setDatas(deviceInfoList);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int positon, long l) {
        clickPositonItem = positon;
        requestPermissions();
    }

    /**
     * 点击进入米粒视频界面
     *
     * @param i
     */
    private void itemClick(int i) {
        DeviceInfo deviceInfo = (DeviceInfo) commonAdapter.getItem(i);
        if (!deviceInfo.isOnline) {
            Toast.makeText(getActivity(), deviceInfo.deviceName, Toast.LENGTH_SHORT).show();
            return;
        }
        DongConfiguration.mDeviceInfo = deviceInfo;
        LogUtils.i("ListActivity.clazz--->>>onItemClick.....2222...deviceInfo:"
                + deviceInfo);
        startActivity(new Intent(getActivity(),
                VideoActiveOpenActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.tv_change)
    public void onViewClicked() {

    }


    private void requestPermissions() {
        PermissionUtils.checkMorePermissions(mContext, locPermissions,
                new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        itemClick(clickPositonItem);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        showExplainDialog(permission, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.requestMorePermissions(mContext, locPermissions, REQUEST_CODE_LOCATION);
                            }
                        });
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.requestMorePermissions(mContext, locPermissions, REQUEST_CODE_LOCATION);
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_CODE_LOCATION:
                PermissionUtils.onRequestMorePermissionsResult(mContext, permissions, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        itemClick(clickPositonItem);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        showExplainDialog(permission, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUtils.requestMorePermissions(mContext, locPermissions, REQUEST_CODE_LOCATION);
                            }
                        });
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        showToAppSettingDialog();
                    }
                });
                break;
        }
    }

    /**
     * 解释权限的dialog
     */
    private void showExplainDialog(String[] permission, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(mContext)
                .setTitle("申请权限")
                .setMessage("APP需要相关的权限才能正常运行")
                .setPositiveButton("确定", onClickListener)
                .show();
    }

    /**
     * 显示前往应用设置Dialog
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("需要权限")
                .setMessage("我们需要相关权限，才能实现功能，点击前往，将转到应用的设置界面，请开启应用的相关权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        yunDuiJIangUtils.onResume();
        ArrayList<DeviceInfo> deviceList = IntercomSDKProxy.requestGetDeviceListFromCache(getActivity());
        commonAdapter.setDatas(dataList);
        LogUtils.e("ListActivity.clazz--->>>onResume.....deviceList:" + deviceList);
    }

    @Override
    public void onPause() {
        super.onPause();
        yunDuiJIangUtils.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
