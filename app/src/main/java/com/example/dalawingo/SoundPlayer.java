package com.example.dalawingo;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {
    public static void play(Context context, int resId) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        if (mp != null) {
            mp.setOnCompletionListener(MediaPlayer::release);
            mp.start();
        }
    }
}
