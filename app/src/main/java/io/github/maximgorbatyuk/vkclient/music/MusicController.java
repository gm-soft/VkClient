package io.github.maximgorbatyuk.vkclient.music;

import android.content.Context;
import android.widget.MediaController;

/**
 * Created by Maxim on 13.05.2016.
 */
public class MusicController extends MediaController {
    public MusicController(Context context) {
        super(context);
    }

    @Override
    public void hide() {
        super.show();
        // show();
    }
}
