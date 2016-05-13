package io.github.maximgorbatyuk.vkclient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.util.ArrayList;

import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.music.IMusicPlayer;
import io.github.maximgorbatyuk.vkclient.music.MusicPlayer;
import io.github.maximgorbatyuk.vkclient.music.PlayerBinder;

public class PlayerService extends Service {

    private MusicPlayer         player;
    private final IBinder binder = new PlayerBinder(this);


    @Override
    public void onCreate() {
        super.onCreate();
        player = new MusicPlayer();

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

    public void setIMusicInterface(IMusicPlayer interfacePlayer){
        player.setIMusicInterface(interfacePlayer);
    }

    public void StopPlaying(){
        player.stopPlaying();
    }

    public void Play(int position){
        player.playAudio(position);
    }

    public int GetPosition(){
        return player.getCurrentPosition();
        // return Position;
    }

    public int GetDuration(){
        return player.getDuration();
    }
    public boolean IsPlaying(){
        return player.isPlaying();
    }
    public void Pause(){
        player.pause();
    }
    public void Seek(int posn){
        player.seekTo(posn);
    }
    public void Go(){
        player.resume();
    }
    public void PlayNext(){
        player.playNext();
    }
    public void PlayPrevious(){
        player.playPrevious();
    }

    public void setRecordList(ArrayList<Audio> source){
        player.setList(source);
    }

    public Audio getCurrentAudio(){
        return player.getCurrentAudio();
    }

    public MediaPlayer getMusicPlayer(){
        return player.getMe();
    }




}
