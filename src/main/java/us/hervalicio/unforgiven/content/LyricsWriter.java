package us.hervalicio.unforgiven.content;

import org.nd4j.linalg.factory.Nd4j;
import us.hervalicio.unforgiven.Config;
import us.hervalicio.unforgiven.neural.Extractor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.nd4j.linalg.api.rng.Random;
import us.hervalicio.unforgiven.neural.NetworkManager;
import us.hervalicio.unforgiven.tumblr.Song;

/**
 * Created by herval on 10/31/15.
 */
public class LyricsWriter {
    private final Extractor inspiredBrain;
    private final Random rnd = Nd4j.getRandom();
    private final TitleMaker titleMaker;


    public static LyricsWriter build(Config config) throws IOException {
        NetworkManager manager = NetworkManager.defaultConfig(config.networkPath);
        manager.load();

        return new LyricsWriter(
                new TitleMaker(new Loader(config.titleFiles, null)),
                new Extractor(manager)
        );
    }

    public LyricsWriter(TitleMaker titles, Extractor contents) throws IOException {
        titleMaker = titles;
        inspiredBrain = contents;
    }

    public Song writeASong() {
        String[] phrases = inspiredBrain.sample(100 + rnd.nextInt(200), 1 + rnd.nextInt(4));

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

    private String makeUpTitle() {
        String title = titleMaker.take(1 + rnd.nextInt(5));
        if(title.contains("(") && !title.contains(")")) {
            title += ")";
        }
        return title;
    }

}
