package com.noahedu.conmonmodule.audio;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import com.noahedu.conmonmodule.utils.GToast;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Description: 音频管理类
 * @Author: huangjialin
 * @CreateDate: 2019/8/28 16:24
 */
public class AudioPlayerManager {
    private final static String TAG = AudioPlayerManager.class.getSimpleName();
    private static AudioPlayerManager INSTANCE;
    private MediaPlayer mMediaPlayer;
    private String currentPlayingPath;
    private boolean isTmpPause = false;
    //    private int curPlayId = -1;
    //private LinkedList<Integer> IdLinkedList;
    private Queue<Integer> idLinkedList;

    private List<MediaStateListener> mListenerList;

    public static AudioPlayerManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AudioPlayerManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioPlayerManager();
                }
            }
        }
        return INSTANCE;
    }

    private AudioPlayerManager() {
        mMediaPlayer = new MediaPlayer();
        mListenerList = new ArrayList<>();
//        IdLinkedList = new LinkedList<>();
        idLinkedList = new LinkedList<>();
    }


    public int getDuration(String audioUrl) {
        if (TextUtils.isEmpty(audioUrl)) {
            return 0;
        }
        int duration = 0;
        // 通过初始化播放器的方式来获取真实的音频长度
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(audioUrl);
            mp.prepare();
            duration = mp.getDuration();
            // 语音长度如果是59s多，因为外部会/1000取整，会一直显示59'，所以这里对长度进行处理，达到四舍五入的效果
            if (duration < 1000) {
                duration = 0;
            } else if (duration >= 60 * 1000) {
                duration = 60 * 1000;
            } else {
                duration = duration + 500;
            }
            mp.release();
        } catch (Exception e) {
            KLog.w(TAG, "getDuration failed", e);
        }
        if (duration < 0) {
            duration = 0;
        }
        return duration;
    }


    public void playAudio(String path, int playId) {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            if (!path.startsWith("http") && (TextUtils.isEmpty(path) || !new File(path).isFile() || !new File(path).exists())) {
                return;
            }
            currentPlayingPath = path;
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
//            stop();
//            curPlayId = playId;
//            IdLinkedList.add(playId);
            idLinkedList.offer(playId);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
                mMediaPlayer.setOnPreparedListener(onPreparedListener);
                mMediaPlayer.setOnCompletionListener(onCompletionListener);
                mMediaPlayer.setOnInfoListener(onInfoListener);
                mMediaPlayer.setOnErrorListener(onErrorListener);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                currentPlayingPath = null;
            }
        }
    }


    public void playAudio(String name, Context mContext, int playId) {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
//            curPlayId = playId;
//            IdLinkedList.add(playId);
            idLinkedList.offer(playId);
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            try {
                AssetFileDescriptor fd = mContext.getAssets().openFd(name);
                mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mMediaPlayer.setOnPreparedListener(onPreparedListener);
                mMediaPlayer.setOnCompletionListener(onCompletionListener);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                GToast.showToast(mContext, "语音文件已损坏或不存在");
                e.printStackTrace();
            }
        }
    }


    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mListenerList != null) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onBufferingComplete(mp, percent);
                }
            }
        }
    };

    /**
     * 音频准备完毕
     */
    int id;
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if(idLinkedList != null && idLinkedList.size() > 0){
                id = idLinkedList.poll();
            }else {
                id = -1;
            }
           /* if (!Utils.isEmpty(IdLinkedList)) {
                id = IdLinkedList.getFirst();
                IdLinkedList.removeFirst();
            } else {
                id = -1;
            }*/
            if (mListenerList != null) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onPrepared(mp, id);
                }
            }
            mp.start();
        }
    };

    /**
     * 播放结束
     */
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mListenerList != null && mListenerList.size() > 0) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onCompletion(mp, id);
                }
            }
        }
    };

    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    Log.i(TAG, "onInfo: ===MEDIA_INFO_METADATA_UPDATE====");
                    break;
                default:
                    Log.i(TAG, "onInfo: ===onInfo====:" + what);
                    break;
            }

            return false;
        }
    };

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "onError: what=" + what);
            return false;
        }
    };

    public void addMediaListener(MediaStateListener listener) {
        if (mListenerList == null) {
            mListenerList = new ArrayList<>();
        }
        if (!mListenerList.contains(listener)) {
            mListenerList.add(listener);
        }
    }

    public void removeMediaListener(MediaStateListener listener) {
        if (mListenerList == null) {
            return;
        }
        if (mListenerList.contains(listener)) {
            mListenerList.remove(listener);
        }
    }

    private void clearAllListener() {
        if (mListenerList != null) {
            mListenerList.clear();
            mListenerList = null;
        }
    }

    public void play() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return;
            }
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
            isTmpPause = false;
            if (mListenerList != null) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onPlayOrPause(true);
                }
            }
        }
    }

    public void pause() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return;
            }
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            if (mListenerList != null) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onPlayOrPause(false);
                }
            }
        }
    }

    public void resume() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return;
            }
            if (isTmpPause) {
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
            }
            isTmpPause = false;
            if (mListenerList != null) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onPlayOrPause(mMediaPlayer.isPlaying());
                }
            }
        }
    }

    public void tmpPause() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return;
            }
            if (mMediaPlayer.isPlaying()) {
                isTmpPause = true;
                mMediaPlayer.pause();
            }
            if (mListenerList != null) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onPlayOrPause(false);
                }
            }
        }
    }

    public void stop() {
        synchronized (mMediaPlayer) {
            KLog.e("hsdfuisifsdf","-------stop--------");
            if (mMediaPlayer == null) {
                return;
            }
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            mMediaPlayer.stop();
//            IdLinkedList.clear();
            idLinkedList.clear();
        }
    }

    public int getCurrentPro() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return 0;
            }
            return mMediaPlayer.getCurrentPosition();
        }
    }

    public long getDuration() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return 0;
            }
            return mMediaPlayer.getDuration();
        }
    }

    public boolean isPlaying() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return false;
            }
            return mMediaPlayer.isPlaying();
        }
    }

    public void autoPlay() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return;
            }
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
            if (mListenerList != null) {
                for (MediaStateListener listener : mListenerList) {
                    listener.onPlayOrPause(mMediaPlayer.isPlaying());
                }
            }
        }
    }

    public String getAudioPath() {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return null;
            }
            return currentPlayingPath;
        }
    }

    public boolean isMatchAudioPath(String path) {
        synchronized (mMediaPlayer) {
            if (mMediaPlayer == null) {
                return false;
            }
            return path != null && currentPlayingPath != null && path.trim().equals(currentPlayingPath);
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            mMediaPlayer.stop();
            mMediaPlayer.setOnBufferingUpdateListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        clearAllListener();
        isTmpPause = false;
        currentPlayingPath = null;
        INSTANCE = null;
//        IdLinkedList.clear();
        idLinkedList.clear();
    }

    public interface MediaStateListener {
        void onBufferingComplete(MediaPlayer mediaPlayer, int percent); //缓冲结束

        void onPrepared(MediaPlayer mediaPlayer, int id);

        void onCompletion(MediaPlayer mediaPlayer, int id);

        void onPlayOrPause(boolean isPlaying);
    }

}
