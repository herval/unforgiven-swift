package us.hervalicio.unforgiven.neural;

import org.apache.commons.io.FileUtils;
import org.deeplearning4j.examples.rnn.CharacterIterator;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * Created by herval on 10/30/15.
 */
public class Example {

    public static void main(String[] args) throws Exception {
        Random rnd = new Random();

        CharacterIterator dataset = loadDataset(32, 300, 50 * 32);

        NetworkManager manager = new NetworkManager(Paths.get("coefficients.bin"), Paths.get("conf.json"));
        Network network = Network.cleanNetwork(dataset);
//        Network network = manager.load();

        Trainer trainer = new Trainer(network, dataset);
        Extractor extractor = new Extractor(network, dataset);

        //Do training, and then generate and print samples from network
        for (int i = 0; i < 100; i++) {
            System.out.println("Starting epoch " + i);
            trainer.fit();

            System.out.println("--------------------");
            System.out.println("Completed epoch " + i);
            System.out.println("Sampling characters from network");
            String[] samples = extractor.sample(300, 1);
//            String[] samples = extractor.sample(200 + rnd.nextInt(100), 1 + rnd.nextInt(10));
            System.out.println("----- Sample -----");
            for (int j = 0; j < samples.length; j++) {
                System.out.println(samples[j]);
            }
        }

        manager.save(network);
        System.out.println("\n\nExample complete");
    }

    private static CharacterIterator loadDataset(int miniBatchSize, int exampleLength, int examplesPerEpoch) throws Exception {
        File taylor = new File("taylor_swift.txt");
        File metallica = new File("metallica.txt");

        List<String> lyrics = FileUtils.readLines(taylor);
        lyrics.addAll(FileUtils.readLines(metallica));

        char[] validCharacters = CharacterIterator.getMinimalCharacterSet();    //Which characters are allowed? Others will be removed
        return new CharacterIterator(
                lyrics,
                miniBatchSize,
                exampleLength,
                examplesPerEpoch,
                validCharacters,
                true
        );
    }

}
