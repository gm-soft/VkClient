package io.github.maximgorbatyuk.vkclient.music;

/**
 * Created by Maxim on 13.05.2016.
 */
public interface IMusicPlayer {

    void onCompletion(int currentPosition);
    void onError(String errorMessage);
}
