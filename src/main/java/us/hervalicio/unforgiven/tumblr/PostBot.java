package us.hervalicio.unforgiven.tumblr;

import us.hervalicio.unforgiven.neural.CharacterMap;
import us.hervalicio.unforgiven.neural.Network;
import us.hervalicio.unforgiven.neural.NetworkManager;

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


    public static void main(String[] args) throws IOException {
        Config conf = new Config();

        CharacterMap characterMap = CharacterMap.getMinimalCharacterMap();
        Network network = new NetworkManager(Paths.get("coefficients.bin"), Paths.get("conf.json")).load(characterMap);
        LyricsWriter writer = new LyricsWriter(network);

        new Thread(
                new PostBot(conf, writer)
        ).run();
    }

}
