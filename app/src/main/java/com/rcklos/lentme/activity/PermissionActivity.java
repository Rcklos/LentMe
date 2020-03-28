package com.rcklos.lentme.activity;

//判断用户是否勾选了不再提醒
//如果拒绝了该权限，并勾选了不再提醒返回false

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rcklos.lentme.utils.OnPermissionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名: PermissionActivity
 * 描述: 动态权限的封装
 * 日期: 2018/10/30
 *
 * @author: liuqiyuan
 */

public class PermissionActivity extends BaseActivity {

    private OnPermissionListener permissionListener = null;
    private static final int PERMISSION_REQ = 10;

    /**
     * 申请权限
     *
     * @param permissions          请求权限
     * @param onPermissionListener 回调函数
     */
    public void reqPermission(String[] permissions, OnPermissionListener onPermissionListener) {
        if (permissions == null || onPermissionListener == null) {
            return;
        }
        this.permissionListener = onPermissionListener;
        //6.0以下不需要申请权限
        if (Build.VERSION.SDK_INT < 23 || permissions.length == 0) {
            onPermissionListener.onGranted();
        } else {
            List<String> permissionList = new ArrayList<>();
            for (String permission : permissions) {
                //校验之前是否添加过权限
                if (ContextCompat.checkSelfPermission(this, permission)
                        == PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                permissionList.add(permission);
            }
            //权限全部都已授权
            if (permissionList.isEmpty()) {
                onPermissionListener.onGranted();
                return;
            }
            //请求权限
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), PERMISSION_REQ);
        }
    }

    /**
     * 申请权限的回调用
     *
     * @param requestCode  请求码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSION_REQ) {
            return;
        }
        if (grantResults.length == 0) {
            return;
        }
        int length = permissions.length;
        //循环判断授权结果
        for (int i = 0; i < length; i++) {
            //授权成功
            if (grantResults[i] == getPackageManager().PERMISSION_GRANTED) {
                continue;
            }
            //用户勾选了"不再提醒"，会返回false,需引导用户手动开启权限
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                showReqPermissionDialog();
            } else {
                permissionListener.onDenied();
            }
            return;
        }
        permissionListener.onGranted();
    }

    /**
     * 提示用户手动开启权限
     */
    private void showReqPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("需要开启权限才能使用此功能")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        goToAppInfo();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                        permissionListener.onDenied();
                    }
                })
                .create()
                .show();
    }

    /**
     * 跳转系统的App应用信息页面
     */
    protected void goToAppInfo() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(localIntent);
    }
}
