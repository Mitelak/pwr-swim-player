package pl.mitelski.player;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {
    private static final String LOG_TAG = "PlayerService";
    public static boolean IS_SERVICE_RUNNING = false;

    private final IBinder binder = new LocalBinder();

    public static Boolean IS_PLAYING = false;
    public static int MAX_DURATION = 0;
    public static int CURRENT_POSITION = 0;
    public static int ACTUAL_ID = 0;
    public static Songs songs = new Songs();

    public static int SCROLL_TIME = 10000;
    public static final String ACTION_CHANGE = "pl.mitelski.player.action.CHANGE";
    public static final String ACTION_PLAY = "pl.mitelski.player.action.PLAY";
    public static final String ACTION_PAUSE = "pl.mitelski.player.action.PAUSE";
    public static final String ACTION_NEXT = "pl.mitelski.player.action.NEXT";
    public static final String ACTION_FORWARD = "pl.mitelski.player.action.FORWARD";
    public static final String ACTION_BACKWARD = "pl.mitelski.player.action.BACKWARD";

    public static MediaPlayer mp = null;
    Notification notification = null;

    void createMediaPlayer() {
        if (mp == null) {
            mp = new MediaPlayer();
//            mp.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mp.setOnCompletionListener(this);
        } else {
            mp.reset();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel chanel = new NotificationChannel("XDD", "XDD", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chanel);
        }

//        processChangeSongRequest(ACTUAL_ID);
//        createMediaPlayer();
        Log.d(LOG_TAG, "Service onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String action = intent.getAction();
//        if (intent.getExtras() != null) {
//            ACTUAL_ID = intent.getExtras().getInt("ID");
//        }
//        Log.d(LOG_TAG, action);

//        switch (action) {
//            case ACTION_CHANGE:
//                Log.d(LOG_TAG, "Clicked Change");
//                processChangeSongRequest(ACTUAL_ID);
//                break;
//            case ACTION_PLAY:
//                Log.d(LOG_TAG, "Clicked Play");
//                processPlayRequest();
//                break;
//            case ACTION_PAUSE:
//                Log.d(LOG_TAG, "Clicked Pause");
//                processPauseRequest();
//                break;
//            case ACTION_FORWARD:
//                Log.d(LOG_TAG, "FORWARD");
//                processForwardRequest();
//                break;
//            case ACTION_BACKWARD:
//                Log.d(LOG_TAG, "FORWARD");
//                processBackwardRequest();
//                break;
//        }
        return START_NOT_STICKY;
    }

    public void backward() {
        mp.seekTo(mp.getCurrentPosition() - SCROLL_TIME );
    }

    public void forward() {
        mp.seekTo(mp.getCurrentPosition() + SCROLL_TIME );
    }

    public void changeSong(int id) {
        ACTUAL_ID = id;
        if (mp != null) {
            mp.stop();
            mp.reset();
            mp.release();
            mp = null;
        }
        IS_PLAYING = false;

        mp = MediaPlayer.create(this, songs.get(ACTUAL_ID).getFileId());

        setUpAsForeground(songs.get(ACTUAL_ID).getTitle(), songs.get(ACTUAL_ID).getArtist());
        MAX_DURATION = mp.getDuration();

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // TODO loop
                if (ACTUAL_ID < songs.size())
                    next();
            }
        });
        Log.d("PLAYER", "changeSong: done");

    }

    private void next() {
        changeSong((ACTUAL_ID + 1) % songs.size());
        play();
    }

    public void play() {
        if (mp == null)
            changeSong(0);
        mp.start();
        IS_PLAYING = true;

    }

    public void pause() {
        mp.pause();
        IS_PLAYING = false;
    }

    public int getCurrentPosition() {
        return mp.getCurrentPosition();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void startForegroundService(){
        Log.d(LOG_TAG, "Start foreground service..");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel chanel = new NotificationChannel("XDD", "XDD", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chanel);
        }

        // default intent notification
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //Notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "XDD");
        mBuilder.setSmallIcon(android.R.drawable.ic_media_play)
        .setContentTitle("Tytuł")
        .setContentText("XDDDddd")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        startForeground(1, mBuilder.build());
    }

    public void stopForegroundService() {
        Log.d(LOG_TAG, "Stop foreground service.");

        stopForeground(true);

        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp.isPlaying())
            mp.stop();
        mp.release();

        Log.i(LOG_TAG, "In onDestrou");
        Toast.makeText(this, "Service destroyed!", Toast.LENGTH_SHORT).show();
    }


    void setUpAsForeground(String title, String artist) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new NotificationCompat.Builder(this,"XDD")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(title)
                .setContentText(artist).build();

//        notification = builder.build();
        startForeground(2, notification);
    }

    public void seekTo(int progress) {
        if (mp != null)
            mp.seekTo(progress);
    }

    public class LocalBinder extends Binder {
        PlayerService getService(){
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
