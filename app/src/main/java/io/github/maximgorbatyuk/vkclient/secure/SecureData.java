package io.github.maximgorbatyuk.vkclient.secure;

import com.vk.sdk.VKScope;

/**
 * Created by Maxim on 24.04.2016.
 */
public class SecureData {

    public static final String SECURE_KEY = "rWxDP6Ku1bCxBO99vyn6";
    public static final String[] scope = new String[]{
            VKScope.AUDIO,
            VKScope.WALL,
            VKScope.MESSAGES,
            VKScope.FRIENDS};
}
