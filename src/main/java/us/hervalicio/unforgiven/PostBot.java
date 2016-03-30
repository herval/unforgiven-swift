package us.hervalicio.unforgiven;

import us.hervalicio.unforgiven.content.LyricsWriter;
import us.hervalicio.unforgiven.tumblr.Client;
import us.hervalicio.unforgiven.content.Song;

import java.io.IOException;

/**
 * Created by herval on 10/31/15.
 */
public class PostBot implements Runnable {
    private final LyricsWriter copywritedContentGenerator;
    private final Client client;
    private final long sleepInterval;

    public PostBot(Config config, LyricsWriter writer) {
        this.client = new Client(config);
        this.copywritedContentGenerator = writer;
        this.sleepInterval = config.sleepInterval;
    }

    @Override
    public void run() {
        while (true) {
            Song sing = copywritedContentGenerator.writeASong();
            try {
                System.out.println(sing);
                client.post(sing.title, sing.lyrics);
            } catch (Exception e) {
                System.out.println("Couldn't post at this time, will retry after the break.");
                e.printStackTrace();
                // TODO handle it
            }

            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // what can one ever do about this exception anyway?
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Config conf = new Config();

        LyricsWriter writer = LyricsWriter.build(conf);

        Thread proc = new Thread(
                new PostBot(conf, writer)
        );
        proc.run();
        proc.join();
    }

}
