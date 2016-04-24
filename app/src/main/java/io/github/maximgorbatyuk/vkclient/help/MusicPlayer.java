package io.github.maximgorbatyuk.vkclient.help;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

/**
 * Created by Maxim on 24.04.2016.
 */
public class MusicPlayer {

    private MediaPlayer player;
    private Context context;

    public MusicPlayer(Context context){
        this.context = context;

    }

    public void Play(Audio record){
        try {
            stopPlaying();
            player = new MediaPlayer();
            String url = record.url;
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

    private void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }


}
