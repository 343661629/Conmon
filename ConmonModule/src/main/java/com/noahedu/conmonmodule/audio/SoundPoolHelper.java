package com.noahedu.conmonmodule.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * @Description: 该类主要是播放一些比较简短的声音，比如按键声，短信声等等  效率会比MediaPlayer高很多
 * @Author: huangjialin
 * @CreateDate: 2020/4/14 9:48
 */
public class SoundPoolHelper {
    private SoundPool mainSoundPool;
    private AudioManager mainAudioManager;
    private float volume;
    private static final int MAX_STREAMS = 5;
    private static final int streamType = AudioManager.STREAM_MUSIC;
    private int soundId;
    private int resId;
    private Context mainContext;

    public SoundPoolHelper(Context context) {
        this.mainContext = context;
    }

    public void playSoundWithRedId(int resId) {
        this.resId = resId;
        init();
    }

    private void init() {
        mainAudioManager = (AudioManager) this.mainContext.getSystemService(Context.AUDIO_SERVICE);
        float currentVolumeIndex = (float) mainAudioManager.getStreamVolume(streamType);
        float maxVolumeIndex = (float) mainAudioManager.getStreamMaxVolume(streamType);
        this.volume = currentVolumeIndex / maxVolumeIndex;
//        ((Activity) this.mainContext).setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.mainSoundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            this.mainSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        this.mainSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                playSound();
            }
        });
        this.soundId = this.mainSoundPool.load(this.mainContext, this.resId, 1);
    }

    private void playSound() {
        float leftVolumn = volume;
        float rightVolumn = volume;
        int streamId = this.mainSoundPool.play(this.soundId, leftVolumn, rightVolumn, 1, 0, 1f);
    }
}
