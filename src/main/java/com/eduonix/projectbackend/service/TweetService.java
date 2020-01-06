package com.eduonix.projectbackend.service;

import com.eduonix.projectbackend.model.Tweet;
import org.springframework.stereotype.Component;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Component
public class TweetService {

    public List<Tweet> getTweets() {

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setTweetModeExtended(true);

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();



        User user = null;
        try {
            user = twitter.verifyCredentials();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        System.out.println(user.getRateLimitStatus());


        List<Tweet>  tweets= new ArrayList<>();
        try {
            convert(twitter, tweets);
        } catch (TwitterException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return tweets;
    }

    private void convert(Twitter twitter, List<Tweet> tweets) throws TwitterException {
        List<Status> statuses;// Get last 100 tweets
        statuses = twitter.getHomeTimeline();

        SimpleDateFormat sdf = getSimpleDateFormat();
        for (Status status : statuses) {
            Tweet tweet = new Tweet();
            tweet.setTime(sdf.format(status.getCreatedAt()));
            tweet.setUser(status.getUser().getName());
            tweet.setScreenName(status.getUser().getScreenName());
            tweet.setText(status.getText());
            tweets.add(tweet);
        }
    }

    private SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Australia/NSW"));
        return sdf;
    }

}
