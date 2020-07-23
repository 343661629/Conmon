package com.noahedu.performancelib.blockcanary;

import com.github.moduth.blockcanary.BlockCanaryContext;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2020/7/23 15:45
 */
public class AppBlockCanaryContext extends BlockCanaryContext {
    private String mUid = "uid";
    private String mNetWorkType = "unknown";
    private int mBlockThreshold = 1 * 1000;
    private String mProvidePath = "/blockcanary/";
    private boolean mDisplayNotification = true;


    public static AppBlockCanaryContext createInstance(){
        return new AppBlockCanaryContext();
    }

    public void setUid(String uid){
        this.mUid = uid;
    }


    public void setNetWorkType(String netWorkType){
        this.mNetWorkType = netWorkType;
    }

    public void setBlockThreshold(int blockThreshold){
        this.mBlockThreshold = blockThreshold;
    }

    public void setProvidePath(String providePath){
        this.mProvidePath = providePath;
    }

    public void setDisplayNotification(boolean displayNotification){
        this.mDisplayNotification = displayNotification;
    }

    /**
     * 设置设备标识符
     * @return
     */
    @Override
    public String provideUid() {
        return mUid;
    }

    /**
     * =设置网络类型
     * @return
     */
    @Override
    public String provideNetworkType() {
        return mNetWorkType;
    }

    /**
     * 设置阈值
     * @return
     */
    @Override
    public int provideBlockThreshold() {
        return mBlockThreshold;
    }

    /**
     * 设置卡顿日志保存路径
     * @return
     */
    @Override
    public String providePath() {
        return mProvidePath;
    }

    /**
     * 设置是否需要通知
     * @return
     */
    @Override
    public boolean displayNotification() {
        return mDisplayNotification;
    }
}
