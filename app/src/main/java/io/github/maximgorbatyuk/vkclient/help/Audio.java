package io.github.maximgorbatyuk.vkclient.help;

/**
 * Created by Maxim on 24.04.2016.
 */
public class Audio {

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

    public Audio(int id, String title, String artist, int duration, String url, int lyrics_id){
        this.id         = id;
        this.title      = title;
        this.artist     = artist;
        this.duration   = duration;
        this.url        = url;
        this.lyrics_id  = lyrics_id;
    }

    @Override
    public String toString() {

        return artist + ": " + title;
        //return super.toString();
    }
}
