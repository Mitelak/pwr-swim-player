package pl.mitelski.player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch loop_switch = findViewById(R.id.loop_switch);
        Switch random_switch = findViewById(R.id.random_switch);

        if (PlayerService.PLAY_LOOP) {
            loop_switch.setChecked(true);
        }

        if (PlayerService.PLAY_RANDOM) {
            loop_switch.setChecked(true);
        }

    }

    public void onLoopClick(View view) {
        if (((Switch) view).isChecked()) {
            PlayerService.PLAY_LOOP = true;
        } else {
            PlayerService.PLAY_LOOP = false;
        }
    }

    public void onRandomClick(View view) {
        if (((Switch) view).isChecked()) {
            PlayerService.PLAY_RANDOM = true;
        } else {
            PlayerService.PLAY_RANDOM = false;
        }
    }
}
