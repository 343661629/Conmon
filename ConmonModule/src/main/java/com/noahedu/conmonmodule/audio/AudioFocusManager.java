package com.noahedu.conmonmodule.audio;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.noahedu.conmonmodule.utils.klog.KLog;


/**
 * @Description: 该工具类主要是用来抢占音频资源使用的
 * @Author: huangjialin
 * @CreateDate: 2020/4/14 16:38
 */
public class AudioFocusManager {
    /**
     * 用AudioManager获取音频焦点避免音视频声音并发问题
     */
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener;


    /**
     * 请求音频焦点 设置监听
     * @param audioListener
     * @param mContext
     * @return
     * AUDIOFOCUS_GAIN：获得音频焦点。
     * AUDIOFOCUS_LOSS：失去音频焦点，并且会持续很长时间。这是我们需要停止MediaPlayer的播放。
     * AUDIOFOCUS_LOSS_TRANSIENT：失去音频焦点，但并不会持续很长时间，需要暂停MediaPlayer的播放，等待重新获得音频焦点。
     * AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK：暂时失去音频焦点，但是无需停止播放，只需降低声音方法。
     *
     */
    public int requestTheAudioFocus(final AudioListener audioListener, Context mContext) {
        //Android 2.2开始(API8)才有音频焦点机制
        if (Build.VERSION.SDK_INT < 8) {
            return 0;
        }
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        }
        if (mAudioFocusChangeListener == null) {
            //监听器
            mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN: //获得音频焦点。
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                            //播放操作
                            audioListener.start();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            //暂停操作
                            audioListener.pause();
                            break;
                        default:
                            KLog.e("huanhshdbfsdf","--------走了默认--------");
                            break;
                    }
                }
            };
        }
        int requestFocusResult = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        return requestFocusResult;
    }


    /**
     * 暂停、播放完成或退到后台释放音频焦点
     */
    public void releaseTheAudioFocus() {
        if (mAudioManager != null && mAudioFocusChangeListener != null) {
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }

    public interface AudioListener{
        void start();
        void pause();
    }


}
