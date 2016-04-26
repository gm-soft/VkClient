package io.github.maximgorbatyuk.vkclient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.Constants;

public class PlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener
{

    private ArrayList<Audio>    RecordList;
    private int                 Position = -1;
    private MediaPlayer         Player;
    private Context             context;
    private final IBinder binder = new PlayerBinder(this);

    public PlayerService() {
    }

    public PlayerService(Context context, ArrayList<Audio> source) {
        this.context = context;
        this.RecordList = source;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {


        return false;
        //return super.onUnbind(intent);
    }

    public void setPosition(int pos){
        this.Position = pos;
    }

    public void StopPlaying(){
        if (Player != null){
            Player.stop();
            Player.release();
            Player = null;
        }
    }

    public void Play(int position){
        try
        {
            StopPlaying();
            String url = RecordList.get(position).url;
            Player = new MediaPlayer();
            Player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Player.setDataSource(url);
            Player.prepareAsync();

            Player.setOnPreparedListener(this);


        } catch (Exception e) {
            Log.d(Constants.LOG_TAG, e.getMessage());
        }
    }

    public int getPosn(){
        return Player.getCurrentPosition();
    }

    public int getDur(){
        return Player.getDuration();
    }

    public boolean isPng(){
        return Player.isPlaying();
    }

    public void pausePlayer(){
        Player.pause();
    }

    public void seek(int posn){
        Player.seekTo(posn);
    }

    public void go(){
        Player.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Position = 0;
        Player = new MediaPlayer();
    }

    public void setRecordList(ArrayList<Audio> source){
        this.RecordList = source;
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Player.start();
        mp.start();
    }

    public class PlayerBinder extends Binder {
        private PlayerService playerService;

        public PlayerBinder(PlayerService playerService) {
            this.playerService = playerService;
        }

        PlayerService getService() {
            return playerService;
        }
    }

}
