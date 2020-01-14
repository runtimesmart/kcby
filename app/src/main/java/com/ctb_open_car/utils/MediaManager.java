package com.ctb_open_car.utils;


import android.content.Context;
import android.media.AudioManager;

public class MediaManager {

    private int vloume;

    private static MediaManager vloumeManager;

    private VloumeListener vloumeListener;

    private PhoneListener phoneListener;

    private MediaManager(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 如果音量发生变化则更改seekbar的位置
        vloume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

    }

    public static MediaManager instance(Context context) {
        if (vloumeManager == null) {
            vloumeManager = new MediaManager(context);
        }
        return vloumeManager;
    }

    public void setVloume(int vloume) {
        this.vloume = vloume;
        if (vloumeListener != null) {
            vloumeListener.onVloume(vloume);
        }
    }

    public void setPhoneState() {
        if (phoneListener != null) {
            phoneListener.pause();
        }
    }

    public void setVloumeListener(VloumeListener vloumeListener) {
        this.vloumeListener = vloumeListener;
    }

    public void setPhoneListener(PhoneListener phoneListener) {
        this.phoneListener = phoneListener;
    }

    public interface VloumeListener {
        void onVloume(int vloume);
    }

    public interface PhoneListener {
        void pause();
    }
}
