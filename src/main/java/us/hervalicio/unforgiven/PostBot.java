package us.hervalicio.unforgiven;

import us.hervalicio.unforgiven.tumblr.Client;
import us.hervalicio.unforgiven.tumblr.Config;
import us.hervalicio.unforgiven.tumblr.LyricsWriter;
import us.hervalicio.unforgiven.tumblr.Song;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by herval on 10/31/15.
 */
public class PostBot implements Runnable {
    private final LyricsWriter copywritedContentGenerator;
    private final Client client;
    private final Config config;

    public PostBot(Config config, LyricsWriter writer) {
        this.client = new Client(config);
        this.copywritedContentGenerator = writer;
        this.config = config;
    }

    @Override
    public void run() {
        while (true) {
            Song sing = copywritedContentGenerator.writeASong();
            try {
                client.post(sing.title, sing.lyrics);
            } catch (Exception e) {
                System.out.println("Couldn't post at this time, will retry after the break.");
                e.printStackTrace();
                // TODO handle it
            }

            try {
                Thread.sleep(config.sleepInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // what can one ever do about this exception anyway?
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Config conf = new Config();

        LyricsWriter writer = new LyricsWriter(Paths.get("networks/150_neurons"));

        Thread proc = new Thread(
                new PostBot(conf, writer)
        );
        proc.run();
        proc.join();
    }

}
