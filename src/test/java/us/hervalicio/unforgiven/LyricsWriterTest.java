package us.hervalicio.unforgiven;

import junit.framework.TestCase;
import us.hervalicio.unforgiven.content.LyricsWriter;

import java.nio.file.Paths;

/**
 * Created by herval on 3/30/16.
 */
public class LyricsWriterTest extends TestCase {

    LyricsWriter writer;

    @Override
    protected void setUp() throws Exception {
        Config conf = new Config();
        conf.networkPath = Paths.get(getClass().getClassLoader().getResource("test_network").toURI());

        writer = LyricsWriter.build(conf);
    }

    public void testTitle() {
        System.out.println(writer.writeASong());
        assertTrue(!writer.writeASong().title.isEmpty());
    }

    public void testLyrics() {
        assertTrue(!writer.writeASong().lyrics.isEmpty());
    }

}
