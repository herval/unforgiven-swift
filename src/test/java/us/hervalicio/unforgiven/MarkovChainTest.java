package us.hervalicio.unforgiven;

import junit.framework.TestCase;
import us.hervalicio.unforgiven.markov.MarkovChain;

import java.util.Arrays;

/**
 * Created by herval on 3/30/16.
 */
public class MarkovChainTest extends TestCase {

    MarkovChain chain = new MarkovChain();

    public void testLoad() {
        chain.load(Arrays.asList("Hello", "world", "again"));
        assertEquals("Hello world again", String.join(" ", chain.take(3)));
    }

}
