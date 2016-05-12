package io.github.maximgorbatyuk.vkclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.github.maximgorbatyuk.vkclient.help.Application;
import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.Constants;

public class PlayerActivity extends AppCompatActivity implements MediaController.MediaPlayerControl{

    private ArrayList<Audio>    RecordList;
    private int                 Position;
    private PlayerService       playerService;
    private Intent              serviceIntent;
    private boolean             serviceBound;
    private Button              playButton;
    private MediaController     controller;

    private TextView titleTextView ;
    private TextView artistTextView ;
    private TextView timeView;
    private TextView durationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Bundle bundle = getIntent().getBundleExtra(Constants.AUDIO_LIST);
        Position = getIntent().getExtras().getInt(Constants.POSITION);
        if (bundle != null) {
            RecordList = bundle.getParcelableArrayList(Constants.AUDIO_LIST);

        }
        playButton      = (Button)   findViewById(R.id.play_button);
        titleTextView   = (TextView) findViewById(R.id.title_text_view);
        artistTextView  = (TextView) findViewById(R.id.artist_text_view);
        timeView        = (TextView) findViewById(R.id.time_view);
        durationView    = (TextView) findViewById(R.id.duration_view);
        /*
        player = new MusicPlayer(this, RecordList);
        player.Play(Position);
        */
    }

    /**
     *
     */
    private void setController(){
        controller = new MediaController(this);
        controller.setMediaPlayer(this);
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
            playButton.setText("Pause");
            fillFields();
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

    private void fillFields(){
        int position = playerService.GetPosition();
        Audio record = RecordList.get(position);

        titleTextView.setText(record.title);
        artistTextView.setText(record.artist);
        timeView.setText(Application.transformDuration( 0 ) );
        durationView.setText(Application.transformDuration( record.duration ));
    }


    private void showNotification(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }



    public void PlayRecord(View view) {
        if (playerService.IsPlaying()) {
            playButton.setText("Play");
            playerService.Pause();

        }
        else {
            playButton.setText("Pause");
            playerService.Go();

        }
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return playerService != null ? playerService.GetDuration() : 0;
        //return 0;
    }

    @Override
    public int getCurrentPosition() {

        return serviceBound ? playerService.GetPosition() : 0;
        //return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (playerService != null)
            playerService.Seek(pos);
    }

    @Override
    public boolean isPlaying() {
        return serviceBound && playerService.IsPlaying();
        //return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
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

    public void PlayNext(View view) {
        playerService.PlayNext();
        fillFields();
    }

    public void PlayPrevious(View view) {
        playerService.PlayPrevious();
        fillFields();
    }
}
