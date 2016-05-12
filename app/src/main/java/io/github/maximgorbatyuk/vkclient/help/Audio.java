package io.github.maximgorbatyuk.vkclient.help;

import android.os.Parcel;
import android.os.Parcelable;

import com.vk.sdk.api.model.VKApiAudio;

/**
 * Created by Maxim on 24.04.2016.
 */
public class Audio implements Parcelable {

    public int id;
    public String title;
    public String artist;
    public int duration;
    public String url;
    public int lyrics_id;



    public Audio(){
        this.id         = -1;
        this.title      = null;
        this.artist     = null;
        this.duration   = 0;
        this.url        = null;
        this.lyrics_id  = -1;
    }

    public Audio(VKApiAudio source){
        this.id          = source.id;
        this.title       = source.title;
        this.artist      = source.artist;
        this.duration    = source.duration;
        this.url         = source.url;
        this.lyrics_id   = source.lyrics_id;
    }

    public Audio(int id, String title, String artist, int duration, String url, int lyrics_id){
        this.id         = id;
        this.title      = title;
        this.artist     = artist;
        this.duration   = duration;
        this.url        = url;
        this.lyrics_id  = lyrics_id;
    }

    protected Audio(Parcel in) {
        id          = in.readInt();
        title       = in.readString();
        artist      = in.readString();
        duration    = in.readInt();
        url         = in.readString();
        lyrics_id   = in.readInt();
    }

    public static final Creator<Audio> CREATOR = new Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };

    @Override
    public String toString() {

        return artist + ": " + title;
        //return super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(      id);
        dest.writeString(   title);
        dest.writeString(   artist);
        dest.writeInt(      duration);
        dest.writeString(   url);
        dest.writeInt(      lyrics_id);
    }
}
