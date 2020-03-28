package com.rcklos.lentme.utils;

/**
 * 类名: OnPermissionListener
 * 描述: 权限申请结果回调
 * 日期: 2018/10/30
 *
 * @author: liuqiyuan
 */
public interface OnPermissionListener {
    /**
     * 已授权
     */
    void onGranted();

    /**
     * 授权失败
     */
    void onDenied();
}