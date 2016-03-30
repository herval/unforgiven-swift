package us.hervalicio.unforgiven;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by herval on 10/31/15.
 */
public class Config {
    public List<File> contentFiles = Arrays.asList(
            new File("inputs/taylor_swift.txt"),
            new File("inputs/metallica.txt")
    );

    public Path networkPath = Paths.get("networks/150_neurons");

    public List<File> titleFiles = Arrays.asList(
            new File(networkPath.toFile(), "taylor_swift_titles.txt"),
            new File(networkPath.toFile(), "metallica_titles.txt")
    );

    public String consumerKey = System.getenv("TUMBLR_CONSUMER_KEY");
    public String blogName = System.getenv("TUMBLR_BLOG_NAME");
    public String oauthToken = System.getenv("TUMBLR_OAUTH_TOKEN");
    public String oauthTokenSecret = System.getenv("TUMBLR_OAUTH_TOKEN_SECRET");
    public String consumerSecret = System.getenv("TUMBLR_CONSUMER_SECRET");
    public long sleepInterval = 12*60*60;
}
