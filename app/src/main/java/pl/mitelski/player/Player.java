package pl.mitelski.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.List;

public class Player {
    static final int SCROLL_TIME = 10000;

    protected MediaPlayer mediaPlayer;
    private int songPos = -1;
    private List<Song> songs;
    private Context context;

    Player(Context context, List<Song> songs) {
        this.context = context;
        this.songs = songs;

        changeSong(0);
        Log.d("PLAYER", "contructor: done");
    }

    void changeSong(int pos) {
        songPos = pos;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(context, songs.get(songPos).getFileId());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // TODO loop
                if (songPos < songs.size())
                    next();
            }
        });
        Log.d("PLAYER", "changeSong: done");
    }

    void play() {
        if (!mediaPlayer.isPlaying() && mediaPlayer != null)
            mediaPlayer.start();
    }

    void pause() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    void seekTo(int time) {
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(time);
        }
    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    boolean isEmpty() {
        return mediaPlayer == null;
    }

    public void forward() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + SCROLL_TIME);
        }
    }

    public void backward() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - SCROLL_TIME);
        }
    }

    void next() {
        songPos = (songPos + 1) % songs.size();
        changeSong(songPos);
        play();
    }
}
