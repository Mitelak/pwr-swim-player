package pl.mitelski.player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
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
    private Player player;

    private SeekBar seekBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initSeekbar();
        getSongs();
        player = new Player(this, songs);
        Log.d(TAG, "onCreate: done");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getSongs() {
        songs.add(new Song("Nostalgia", "Taco Hemingway", R.raw.nostalgia));
        songs.add(new Song("Chodź", "Taco Hemingway", R.raw.chodz));
    }

    @Override
    public void onItemClick(View view, int pos) {
        Log.d("click", "clicked: " + pos);

        player.changeSong(pos);
//        mediaPlayer = MediaPlayer.create(this, sAdapter.getItem(pos).getFileId());
//        mediaPlayer.start();
//        player.play();
        buttonPlayPause(findViewById(R.id.play_pause));
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recycler_songs);
        sAdapter = new SongsAdapter(songs);
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
                            player.seekTo(progress);
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
//        if (player.isEmpty() && sAdapter.getItemCount() > 0) {
//            player.changeSong(sAdapter.getItem(0), 0);
//        }

        if (!player.isPlaying()) {
            player.play();
            ((ImageButton) view).setImageResource(R.drawable.ic_pause_black_24dp);
            seekBar.setMax(player.mediaPlayer.getDuration());
        } else if (player.isPlaying()) {
            player.pause();
            ((ImageButton) view).setImageResource(R.drawable.ic_play_arrow_black_24dp);
            Log.d(TAG, "isPlaying: " + player.mediaPlayer.getDuration());
        }


        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!player.isEmpty()) {
                    int currentSeekPosition = player.mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentSeekPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    void buttonForward(View view) {
        player.forward();
    }

    void buttonBackward(View view) {
        player.backward();
    }
}
