package io.github.maximgorbatyuk.vkclient.help;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Maxim on 24.04.2016.
 */
public class MusicPlayer {

    private MediaPlayer player;
    private Context context;
    private List<Audio> source;
    private int Position = -1;

    public MusicPlayer(Context context){
        this.context = context;
    }

    public void Play(int pos){
        if (Position != pos) {
            Position = pos;
            startPlaying(pos);
        } else {
            player.start();
        }
    }

    private void StopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    private void startPlaying(int position){
        try
        {
            StopPlaying();
            String url = source.get(position).url;
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(url);
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }


            });
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void PlayNext(){
        Position++;
        startPlaying(Position);
    }

    public void Pause(){
        if (player != null)
            player.pause();
    }

    public void Stop(){
        StopPlaying();
    }


    public void setSource(List<Audio> source) {
        this.source = source;
    }
}
