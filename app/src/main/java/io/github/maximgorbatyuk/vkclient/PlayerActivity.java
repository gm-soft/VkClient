package io.github.maximgorbatyuk.vkclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.Constants;

public class PlayerActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private ArrayList<Audio>    RecordList;
    private int                 Position;
    private PlayerService       playerService;
    private Intent              serviceIntent;
    private boolean             serviceBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Bundle bundle = getIntent().getBundleExtra(Constants.AUDIO_LIST);
        Position = getIntent().getExtras().getInt(Constants.POSITION);
        if (bundle != null) {
            RecordList = bundle.getParcelableArrayList(Constants.AUDIO_LIST);

        }

        /*
        player = new MusicPlayer(this, RecordList);
        player.Play(Position);
        */
    }


    // Поприетарный код как-то
    private ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder) service;
            playerService = binder.getService();
            playerService.setRecordList(RecordList);
            playerService.setContext(getApplicationContext());
            serviceBound = true;
            playerService.setPosition(Position);
            playerService.Play(Position);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (serviceIntent == null){
            serviceIntent = new Intent(this,  PlayerService.class);
            bindService(serviceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
            startService(serviceIntent);

        }
    }

    @Override
    protected void onDestroy() {
        playerService.StopPlaying();
        if (serviceBound)
            unbindService(playerServiceConnection);
        stopService(serviceIntent);
        serviceIntent = null;
        super.onDestroy();
    }

    private void fillFields(int position){
        Audio record = RecordList.get(position);
        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        titleTextView.setText(record.title);
        TextView artistTextView = (TextView) findViewById(R.id.artist_text_view);
        artistTextView.setText(record.artist);
    }

    @Override
    public void start() {
        /*
        if (player == null) return;
        player.Play(Position);
        fillFields(Position);
        */

    }

    @Override
    public void pause() {
        /*
        if (player == null) return;
        player.Pause();
        */

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void showNotification(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


}
