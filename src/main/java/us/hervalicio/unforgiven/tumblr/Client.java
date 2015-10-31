package us.hervalicio.unforgiven.tumblr;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.TextPost;

/**
 * Created by herval on 10/31/15.
 */
public class Client {

    private final JumblrClient client;
    private final String blogName;

    public Client(Config config) {
        // Create a new client
        this.client = new JumblrClient(config.consumerKey, config.consumerSecret);
        client.setToken(config.oauthToken, config.oauthTokenSecret);
        this.blogName = config.blogName;
    }

    public void post(String title, String content) throws InstantiationException, IllegalAccessException {
        TextPost post = client.newPost(blogName, TextPost.class);
        post.setBody(content);
        post.setTitle(title);
        post.save();
    }
}
