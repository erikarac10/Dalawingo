package com.example.dalawingo.ui.game.shared;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {

    public static void play(Context context, int resId) {
        play(context, resId, 1.0f); // full volume
    }

    public static void play(Context context, int resId, float volume) {
        MediaPlayer mp = MediaPlayer.create(context, resId);
        if (mp != null) {
            mp.setVolume(volume, volume); // L/R channels
            mp.setOnCompletionListener(MediaPlayer::release);
            mp.start();
        }
    }
}