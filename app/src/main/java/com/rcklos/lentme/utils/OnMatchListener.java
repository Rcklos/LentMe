package com.rcklos.lentme.utils;

/**
 * 匹配监听器回调接口
 * 2020年1月5日 08点52分
 */
public interface OnMatchListener {
    /**
     * 匹配成功
     */
    void onMatch();

    /**
     * 匹配失败
     */
    void onFail();
}
