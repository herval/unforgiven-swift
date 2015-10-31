package us.hervalicio.unforgiven.tumblr;

import us.hervalicio.unforgiven.neural.Extractor;
import us.hervalicio.unforgiven.neural.Network;

/**
 * Created by herval on 10/31/15.
 */
public class LyricsWriter {
    private final Extractor inspiredBrain;

    public LyricsWriter(Network neuralNetwork) {
        inspiredBrain = neuralNetwork.extractor();
    }

    public Song writeASong() {
        String title = inspiredBrain.sample(20, 1)[0];
        String[] phrases = inspiredBrain.sample(300, 20);

        return new Song(
                title,
                String.join("\n", phrases)
        );
    }
}
