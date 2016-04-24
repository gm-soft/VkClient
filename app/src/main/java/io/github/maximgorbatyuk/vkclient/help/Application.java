package io.github.maximgorbatyuk.vkclient.help;

import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import java.util.concurrent.TimeUnit;

import io.github.maximgorbatyuk.vkclient.MainActivity;
import io.github.maximgorbatyuk.vkclient.secure.SecureData;

/**
 * Created by Maxim on 24.04.2016.
 */
public class Application extends android.app.Application {


    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                // VkAccessToken is invalid
                Intent intent = new Intent(Application.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);

    }

    public static String transformDuration(int duration){
        long minutes    = TimeUnit.SECONDS.toMinutes(duration);
        long seconds    = duration - TimeUnit.MINUTES.toSeconds(minutes);
        String mm = minutes < 10 ? "0" + minutes : "" + minutes;
        String ss = seconds < 10 ? "0" + seconds : "" + seconds;

        return mm + ":" + ss;
    }
}
