package com.example.lab09_backgroundservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    public static final String ACTION_START = "com.example.lab09_backgroundservice.action.START";
    public static final String ACTION_STOP = "com.example.lab09_backgroundservice.action.STOP";
    public static final String EXTRA_AUDIO_URI = "extra_audio_uri";

    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }

        String action = intent.getAction();
        if (ACTION_START.equals(action)) {
            String audioUriString = intent.getStringExtra(EXTRA_AUDIO_URI);
            if (audioUriString == null || audioUriString.isEmpty()) {
                Toast.makeText(this, R.string.pick_audio_first, Toast.LENGTH_SHORT).show();
                return START_NOT_STICKY;
            }
            startMusic(Uri.parse(audioUriString));
            return START_STICKY;
        }

        if (ACTION_STOP.equals(action)) {
            stopMusic();
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void startMusic(Uri audioUri) {
        stopMusic();
        mediaPlayer = MediaPlayer.create(this, audioUri);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            Toast.makeText(this, R.string.music_started, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.error_play_music, Toast.LENGTH_SHORT).show();
        }
    }

    private void stopMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, R.string.music_stopped, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        stopMusic();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
