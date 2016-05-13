package io.github.maximgorbatyuk.vkclient.music;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;

import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.Constants;

/**
 * Created by Maxim on 13.05.2016.
 */
public class MusicPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private ArrayList<Audio>    list;
    private int                 position;
    private boolean             random;
    private MediaPlayer         player;
    private IMusicPlayer        delegate;

    public MusicPlayer(){
        this.list = null;
        position = 0;
        random = false;
    }

    public MusicPlayer(ArrayList<Audio> list) {
        this.list = list;
        position = 0;
        random = false;
        // this.delegate = delegate;
    }

    public ArrayList<Audio> getList() {
        return list;
    }
    public void setList(ArrayList<Audio> list) {
        this.list = list;
    }

    public void playAudio(int position){
        try
        {
            stopPlaying();
            this.position = position;
            String url = list.get(position).url;

            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepareAsync();

            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);


        } catch (Exception e) {
            Log.d(Constants.LOG_TAG, "Error while playing preparing: " + e.getMessage());
            delegate.onError("Error while playing preparing: " + e.getMessage());
        }
    }

    public Audio getCurrentAudio(){
        return list.get(position);
    }

    public void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void playNext(){
        position = position == list.size() - 1 ? 0 : position + 1;
        playAudio(position);
    }

    public void playPrevious(){
        position = position == 0 ? list.size() -1 : position - 1;
        playAudio(position);
    }

    public void setRandomize(boolean random) {
        this.random = random;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        delegate.onCompletion(position);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public int getDuration() {
        return player != null? player.getDuration() : 0;
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void pause() {
        player.pause();
    }

    public void seekTo(int ms) {
        player.seekTo(ms);
    }

    public void resume() {
        player.start();
    }

    public void setIMusicInterface(IMusicPlayer IMusicInterface) {
        this.delegate = IMusicInterface;
    }

    public MediaPlayer getMe() {
        return player;
    }

    public int getCurrentPosition() {
        return player != null ? player.getCurrentPosition() : 0;
    }
}
