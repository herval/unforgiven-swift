package us.hervalicio.unforgiven.tumblr;

/**
 * Created by herval on 10/31/15.
 */
public class Config {
    public String consumerKey = System.getenv("TUMBLR_CONSUMER_KEY");
    public String blogName = System.getenv("TUMBLR_BLOG_NAME");
    public String oauthToken = System.getenv("TUMBLR_OAUTH_TOKEN");
    public String oauthTokenSecret = System.getenv("TUMBLR_OAUTH_TOKEN_SECRET");
    public String consumerSecret = System.getenv("TUMBLR_CONSUMER_SECRET");
    public long sleepInterval = 12*60*60;
}
