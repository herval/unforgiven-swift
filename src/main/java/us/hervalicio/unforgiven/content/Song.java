package us.hervalicio.unforgiven.content;

/**
 * Created by herval on 10/31/15.
 */
public class Song {
    public final String title;
    public final String lyrics;

    public Song(String title, String lyrics) {
        this.title = title;
        this.lyrics = lyrics;
    }

    @Override
    public String toString() {
        return title + "\n" + lyrics;
    }
}
