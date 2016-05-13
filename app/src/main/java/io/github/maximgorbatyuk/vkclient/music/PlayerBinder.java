package io.github.maximgorbatyuk.vkclient.music;

import android.os.Binder;

import io.github.maximgorbatyuk.vkclient.PlayerService;

/**
 * Created by Maxim on 13.05.2016.
 */
public class PlayerBinder extends Binder {

    private PlayerService playerService;
    public PlayerBinder(PlayerService playerService) {
        this.playerService = playerService;
    }
    public PlayerService getService() {
        return playerService;
    }
}
