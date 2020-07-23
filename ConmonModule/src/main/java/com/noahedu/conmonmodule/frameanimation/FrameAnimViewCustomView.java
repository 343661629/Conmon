package com.noahedu.conmonmodule.frameanimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;


import com.noahedu.conmonmodule.frameanimation.util.AnimaUtils;
import com.noahedu.conmonmodule.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: huangjialin
 * @CreateDate: 2019/9/5 9:42
 */
public class FrameAnimViewCustomView extends FrameAnimView {
    private PlayerFinsh playerFinsh;
    private List<Integer> resIdsList = new ArrayList<Integer>();
    private List<Integer> resTransitionList = new ArrayList<Integer>();
    float rawY = 0;
    float rawX = 0;
    float rawYUp = 0;
    float rawXUP = 0;

    public FrameAnimViewCustomView(Context context) {
        super(context);
    }

    public FrameAnimViewCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameAnimViewCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setImageFramePlayerFinsh(PlayerFinsh playerFinsh) {
        if (null != playerFinsh) {
            this.playerFinsh = playerFinsh;
        }
    }


    public void start() {
        if (null != mAnimation) {
            mAnimation.start();
        }
    }


    public void pause() {
        if (null != mAnimation) {
            mAnimation.pause();
        }
    }

    public void stop() {
        if (null != mAnimation) {
            resIdsList.clear();
            resTransitionList.clear();
            mAnimation.stop();
            mAnimation = null;
        }
    }

    public boolean isRunning() {
        if (null != mAnimation) {
           return mAnimation.isRunning();
        }
        return false;
    }


    /**
     * 不需要过渡动画
     */
    public void init(final int id, boolean loop, int fly) {
        mAnimation = new FrameAnimation.FrameAnimationBuilder(getResources())
                .setLoop(loop)
                .setResIds(getRes(id))
                .setDuration(fly)
                .isLowMemory(true)
                .build();
        mAnimation.setOnImageLoadListener(new OnImageLoadListener() {
            @Override
            public void onImageLoad(BitmapDrawable drawable) {
                FrameAnimViewCustomView.this.setImageDrawable(drawable);
            }

            @Override
            public void onFinish() { //只有设置 loop 为false的时候才起作用
                if (null != playerFinsh) {
                    playerFinsh.onPlayFinish(id);
                }
            }
        });
    }

    /**
     * @param id 主动画id
     * @param tranIds 过渡动画id
     * @param loop 是否需要循环播放动画
     * @param fly 帧率
     */
    public void init(final int id,final int tranIds, boolean loop, int fly) {
        mAnimation = new FrameAnimation.FrameAnimationBuilder(getResources())
                .setLoop(loop)
                .setResIds(getRes(id))
                .setResTransitionIds(getTransitionRes(tranIds))
                .setDuration(fly)
                .isLowMemory(true)
                .build();
        mAnimation.setOnImageLoadListener(new OnImageLoadListener() {
            @Override
            public void onImageLoad(BitmapDrawable drawable) {
                FrameAnimViewCustomView.this.setImageDrawable(drawable);
            }

            @Override
            public void onFinish() { //只有设置 loop 为false的时候才起作用
                if (null != playerFinsh) {
                    playerFinsh.onPlayFinish(id);
                }
            }
        });
    }




    /*private int[] getRes(int id) {
        TypedArray typedArray = getResources().obtainTypedArray(id);
        int len = typedArray.length();
        int[] resId = new int[len];
        for (int i = 0; i < len; i++) {
            resId[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return resId;
    }*/

    private List<Integer> getRes(int id) { //resIdsList
        TypedArray typedArray = getResources().obtainTypedArray(id);
        int len = typedArray.length();
        for (int i = 0; i < len; i++) {
            resIdsList.add(typedArray.getResourceId(i, -1));
        }
        typedArray.recycle();
        return resIdsList;
    }

    private List<Integer> getTransitionRes(int id) { //resIdsList
        TypedArray typedArray = getResources().obtainTypedArray(id);
        int len = typedArray.length();
        for (int i = 0; i < len; i++) {
            resTransitionList.add(typedArray.getResourceId(i, -1));
        }
        typedArray.recycle();
        return resTransitionList;
    }



    private int [] getLocalRes(String imageName, int imageCount){
        return null;
    }



    public interface PlayerFinsh {
        void onPlayFinish(int id);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        String values = AnimaUtils.getSystemProperties(AnimaUtils.FULLSCREEN_SWITCH_PROPERTY);
        if (AnimaUtils.stringToInt(values,0) == 1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    rawY = event.getRawY();
                    rawX = event.getRawX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    rawYUp = event.getRawY();
                    rawXUP = event.getRawX();
                    float distanceY = rawYUp - rawY;
                    float distanceX = Math.abs(rawXUP - rawX);
                    //下滑
                    if (distanceY > 10 && distanceY > distanceX) {
                        if (mDownScrollListener != null) {
                            mDownScrollListener.downScroll();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    performClick();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }


    public interface DownScrollListener {
        void downScroll();
    }

    private DownScrollListener mDownScrollListener;

    public void setDownScrollListener(DownScrollListener listener) {
        this.mDownScrollListener = listener;
    }


}
