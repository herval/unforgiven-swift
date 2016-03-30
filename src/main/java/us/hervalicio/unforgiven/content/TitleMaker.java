package us.hervalicio.unforgiven.content;

import us.hervalicio.unforgiven.markov.MarkovChain;

import java.util.Arrays;

/**
 * Created by herval on 3/30/16.
 */
public class TitleMaker {
    private final MarkovChain chainsOfCreativity = new MarkovChain();

    public TitleMaker(Loader contentLoader) {
        for(String line : contentLoader.lines()) {
            chainsOfCreativity.load(Arrays.asList(line.split(" ")));
        }
    }

    public String take(int q) {
        return String.join(" ", chainsOfCreativity.take(q));
    }
}
