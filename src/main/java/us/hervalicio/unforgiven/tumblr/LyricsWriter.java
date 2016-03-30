package us.hervalicio.unforgiven.tumblr;

import org.nd4j.linalg.factory.Nd4j;
import us.hervalicio.unforgiven.neural.Extractor;
import us.hervalicio.unforgiven.neural.Network;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.nd4j.linalg.api.rng.Random;
import us.hervalicio.unforgiven.neural.NetworkManager;

/**
 * Created by herval on 10/31/15.
 */
public class LyricsWriter {
    private final Extractor inspiredBrain;
    private final Random rnd = Nd4j.getRandom();

    public LyricsWriter(Path networkPath) throws IOException {
        NetworkManager manager = NetworkManager.defaultConfig(networkPath);
        manager.load();

        inspiredBrain = new Extractor(manager);
    }

    public String makeUpTitle() {
        String title = inspiredBrain.sample(10 + rnd.nextInt(50), 1)[0];

        // TODO remove characters?
        return title;
    }

    public Song writeASong() {
        String[] phrases = inspiredBrain.sample(100 + rnd.nextInt(200), 1+rnd.nextInt(4));

        // pick up 1 - 8 verses
        int verses = rnd.nextInt(8);
        List<String> finalLyrics = new ArrayList<>();
        int pickedVerses = 0;
        for (String phrase : phrases) {
            finalLyrics.add(phrase);
            if (phrase.isEmpty()) {
                pickedVerses++;
                if (pickedVerses == verses) {
                    break;
                }
            }
        }

        String lyrics = String.join("\n", finalLyrics).trim();

        return new Song(
                makeUpTitle(),
                lyrics
        );
    }
}
