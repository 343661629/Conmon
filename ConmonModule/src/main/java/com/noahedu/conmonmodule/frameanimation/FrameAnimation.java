package com.noahedu.conmonmodule.frameanimation;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.noahedu.conmonmodule.utils.Utils;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static android.R.attr.height;
import static android.R.attr.width;

/**
 * 帧动画动画类
 *
 * @author haohao on 2017/6/27 14:28
 * @version v1.0
 */
public class FrameAnimation {

    private static final int MSG_REFRESH = 1;

    private static HandlerThread mThread;
    private Handler handler;

    public static final float DEFAULT_DURATION = 15f;

//    private int[] resIds;//动画列表
//    private int[] resTransitionIds;

    private List<Integer> resIdsList = new ArrayList<Integer>(); //主动画列表
    private List<Integer> resTransitionIdsList = new ArrayList<Integer>(); //过渡动画列表
    private List<Integer> allAnimationImageList = new ArrayList<Integer>();

    private volatile float duration = DEFAULT_DURATION;//动画间隔时间
    private boolean loop = false;//是否循环
    private boolean isRunning = false;
    private boolean needStop = false;


    private volatile int index = 0;//当前显示图片
    private ImageCache imageCache;
    private volatile BitmapDrawable bitmapDrawable;
    private OnImageLoadListener onImageLoadListener;
    private Resources resources;

    private Runnable mUpateRunnable = new Runnable() {
        @Override
        public void run() {
            mUpdateMainHandler.sendEmptyMessage(MSG_REFRESH);
        }
    };

    private Handler mUpdateMainHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REFRESH) {
                if (onImageLoadListener != null) {
                    onImageLoadListener.onImageLoad(bitmapDrawable);
                    bitmapDrawable = null;
                }
                loadInThreadFromRes();
            }
        }
    };


    FrameAnimation(Resources resources, List<Integer> resList, List<Integer> resTransitionIds, boolean loop, float duration, boolean isLowMemory) {
        if (null != imageCache) {
            imageCache.destory();
        }
        imageCache = new ImageCache(isLowMemory);
        this.resources = resources;
//        this.resIds = resIds;
//        this.resTransitionIds = resTransitionIds;

        resIdsList.clear(); //主动画
        resTransitionIdsList.clear(); //过渡动画
        allAnimationImageList.clear();
        if(!Utils.isEmpty(resList)){
            resIdsList.addAll(resList);
        }
        if (!Utils.isEmpty(resTransitionIds)) {
            resTransitionIdsList.addAll(resTransitionIds);
        }
        if (!Utils.isEmpty(resIdsList)) {
            allAnimationImageList.addAll(resIdsList);
        }
        if (!Utils.isEmpty(resTransitionIdsList)) {
            allAnimationImageList.addAll(0,resTransitionIdsList);
        }
        this.loop = loop;
        this.duration = duration;

        if (mThread == null) {
            mThread = new HandlerThread("frame bitmap");
            mThread.start();
        }

        handler = new Handler(mThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                mUpdateMainHandler.sendEmptyMessage(MSG_REFRESH);
            }
        };
    }

    public void setResIds(int[] resIds) {
        //this.resIds = resIds;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void start() {
        if (isRunning) {
            return;
        }
        loadInThreadFromRes();
    }

    public void pause() {
        if (!isRunning) {
            return;
        }
        needStop = true;
        isRunning = false;
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        resIdsList.clear();
        resTransitionIdsList.clear();
        allAnimationImageList.clear();
        index = 0;
        needStop = true;
        isRunning = false;
        //handler.getLooper().quitSafely();
        imageCache.destory();
        handler.removeCallbacksAndMessages(null);
        mUpdateMainHandler.removeCallbacksAndMessages(null);
    }

    private void loadInThreadFromRes() {
        /*if (resIds == null || resIds.length == 0) {
            isRunning = false;
            return;
        }*/
        if (Utils.isEmpty(resIdsList)) {
            isRunning = false;
            return;
        }
        if (needStop) {
            isRunning = false;
            needStop = false;
            return;
        }
        isRunning = true;
//        if (index < resIds.length) {
//            int resId = resIds[index];
        if (index < allAnimationImageList.size()) {
            int resId = allAnimationImageList.get(index);
            if (bitmapDrawable != null) {
                imageCache.mReusableBitmaps.add(new SoftReference<>(bitmapDrawable.getBitmap()));
            }
            long start = System.currentTimeMillis();
            bitmapDrawable =
                    BitmapLoadUtil.decodeBitmapFromResLruCache(resources, resId, width,
                            height,
                            imageCache);
            long end = System.currentTimeMillis();
            float updateTime = (duration - (end - start)) > 0 ? (duration - (end - start)) : 0;
            Message message = Message.obtain();
//            message.obj = resIds;
            message.obj = allAnimationImageList;
            handler.sendMessageAtTime(message,
                    index == 0 ? 0 : (int) (SystemClock.uptimeMillis() + updateTime));
            index++;
        } else {
            if (loop) { //是否循环播放
                index = 0; //注意：如果需要设置从某一段开始循环，那么需要修改这个地方的index
                if (!Utils.isEmpty(resTransitionIdsList)) {
                    allAnimationImageList.removeAll(resTransitionIdsList);
                    //index = resTransitionIdsList.size();
                }
                loadInThreadFromRes();
            } else {
                index++;
                bitmapDrawable = null;
                duration = 0;
                if (onImageLoadListener != null) {
                    onImageLoadListener.onFinish();
                }
                isRunning = false;
                onImageLoadListener = null;
            }
        }
    }

    public void setOnImageLoadListener(OnImageLoadListener onImageLoadListener) {
        this.onImageLoadListener = onImageLoadListener;
    }

    public static class FrameAnimationBuilder {

        private Resources resources;
        private float duration = DEFAULT_DURATION;//动画间隔时间
        private boolean loop = false;//是否循环
        //        private int[] resIds;//动画列表
        private List<Integer> resIdsList;
        private List<Integer> resTransitionList;
        private int[] resTransitionIds;
        private boolean isLowMemory;//低内存、少量图片时设置，防止图片不显示

        public FrameAnimationBuilder(@NonNull Resources resources) {
            this.resources = resources;
        }

        /*public FrameAnimationBuilder setResIds(int[] resIds) {
            this.resIds = resIds;
            return this;
        }*/

        public FrameAnimationBuilder setResIds(List<Integer> resIdsList) {
//            this.resIds = resIds;
            this.resIdsList = resIdsList;
            return this;
        }

        /**
         * 添加过渡动画
         */
        /*public FrameAnimationBuilder setResTransitionIds(int[] resTransitionIds) {
            this.resTransitionIds = resTransitionIds;
            return this;
        }*/
        public FrameAnimationBuilder setResTransitionIds(List<Integer> resTransition) {
            this.resTransitionList = resTransition;
            return this;
        }


        public FrameAnimationBuilder setLoop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public FrameAnimationBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public FrameAnimationBuilder isLowMemory(boolean isLowMemory) {
            this.isLowMemory = isLowMemory;
            return this;
        }


        public FrameAnimation build() {
            return new FrameAnimation(resources, resIdsList, resTransitionList, loop, duration, isLowMemory);
        }

    }
}
