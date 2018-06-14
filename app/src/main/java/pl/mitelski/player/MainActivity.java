package pl.mitelski.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SongsAdapter.ItemClickListener {
    public static final String TAG = "MainActivity";

    private List<Song> songs = new ArrayList<>();
    private RecyclerView recyclerView;
    private SongsAdapter sAdapter;

    PlayerService playerService;
    boolean bound = false;

    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initSeekbar();
//        songs = Songs.SONGS;

        Intent intent = new Intent(this, PlayerService.class);
        this.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        this.startService(intent);
        Log.d(TAG, "onCreate: done");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound)
            unbindService(connection);
        bound = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemClick(View view, int pos) {
        Log.d("click", "clicked: " + pos);

//        Intent intent = new Intent(MainActivity.this, PlayerService.class);
//        intent.putExtra("ID",  pos);
//        startService(intent.setAction(PlayerService.ACTION_CHANGE));
//        startService(intent.setAction(PlayerService.ACTION_PLAY));
        playerService.changeSong(pos);
//        playerService.play();
        buttonPlayPause(findViewById(R.id.play_pause));
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recycler_songs);
        sAdapter = new SongsAdapter();
        sAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        seekBar = findViewById(R.id.seekBar);
    }

    void initSeekbar() {
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                          playerService.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                }
        );
    }

    void buttonPlayPause(View view) {
        // start service
//        Intent intent = new Intent(MainActivity.this, PlayerService.class);
        Log.d(TAG, "PLAY_PAUSE");
        if (!PlayerService.IS_PLAYING) {
            Log.d(TAG, "PLAY");
//            startService(intent.setAction(PlayerService.ACTION_PLAY));
            playerService.play();
            ((ImageButton) view).setImageResource(android.R.drawable.ic_media_pause);

            seekBar.setMax(PlayerService.MAX_DURATION);
        } else if (PlayerService.IS_PLAYING) {

            Log.d(TAG, "PAUSE");
            playerService.pause();
//            startService(intent.setAction(PlayerService.ACTION_PAUSE));
            ((ImageButton) view).setImageResource(android.R.drawable.ic_media_play);
        }


        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int currentSeekPosition = playerService.getCurrentPosition();
                seekBar.setProgress(currentSeekPosition);
                handler.postDelayed(this, 1000);
            }
        });
    }

    void buttonForward(View view) {
//        Intent intent = new Intent(MainActivity.this, PlayerService.class);
//        startService(intent.setAction(PlayerService.ACTION_FORWARD));
        playerService.forward();
    }

    void buttonBackward(View view) {
        playerService.backward();
    }


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            playerService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}
