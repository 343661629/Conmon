package com.noahedu.performancelib.blockcanary;

import android.content.Context;

import com.github.moduth.blockcanary.BlockCanary;

/**
 * @Description: 检测卡顿工具 BlockCanary
 * @Author: huangjialin
 * @CreateDate: 2020/7/23 15:40
 */
public class InitBlockCanary {
    private static InitBlockCanary mInitBlockCanary;
    private Context mContext;
    private AppBlockCanaryContext appBlockCanaryContext;


    private InitBlockCanary(Context mContext) {
        this.mContext = mContext;
        appBlockCanaryContext = new AppBlockCanaryContext();
    }

    public static InitBlockCanary createInstance(Context context) {
        mInitBlockCanary = new InitBlockCanary(context);
        return mInitBlockCanary;
    }

    public InitBlockCanary setUid(String uid) {
        if (null == appBlockCanaryContext) {
            throw new RuntimeException("you must create InitBlockCanary");
        }
        appBlockCanaryContext.setUid(uid);
        return this;
    }


    public InitBlockCanary setNetWorkType(String netWorkType) {
        if (null == appBlockCanaryContext) {
            throw new RuntimeException("you must create InitBlockCanary");
        }
        appBlockCanaryContext.setNetWorkType(netWorkType);
        return this;
    }

    public InitBlockCanary setBlockThreshold(int blockThreshold) {
        if (null == appBlockCanaryContext) {
            throw new RuntimeException("you must create InitBlockCanary");
        }
        appBlockCanaryContext.setBlockThreshold(blockThreshold);
        return this;
    }

    public InitBlockCanary setProvidePath(String providePath) {
        if (null == appBlockCanaryContext) {
            throw new RuntimeException("you must create InitBlockCanary");
        }
        appBlockCanaryContext.setProvidePath(providePath);
        return this;
    }

    public InitBlockCanary setDisplayNotification(boolean displayNotification) {
        if (null == appBlockCanaryContext) {
            throw new RuntimeException("you must create InitBlockCanary");
        }
        appBlockCanaryContext.setDisplayNotification(displayNotification);
        return this;
    }

    public void start() {
        if (null == appBlockCanaryContext) {
            throw new RuntimeException("you must create InitBlockCanary");
        }
        BlockCanary.install(mContext, appBlockCanaryContext).start();
    }


}
