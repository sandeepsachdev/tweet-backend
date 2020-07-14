package com.eduonix.projectbackend.service;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import com.eduonix.projectbackend.model.Book;
import com.eduonix.projectbackend.model.Tweet;
import com.eduonix.projectbackend.repository.BookRepository;
import com.twitter.twittertext.Autolink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.exit;

@Component
public class TweetService {

    @Autowired
    private BookRepository repository;

    @Cacheable("tweets")
    public List<Tweet> getTweets() {

        RssReader reader = new RssReader();
        Stream<Item> rssFeed = null;

        {
            try {
                rssFeed = reader.read("https://www.smh.com.au/rss/national/nsw.xml");
            } catch (IOException e) {
                e.printStackTrace();
                exit(0);
            }
        }


        List<Item> articles = rssFeed.collect(Collectors.toList());

        for (Item item: articles) {
            System.out.println(item.getLink().toString());
        }

        repository.save(new Book("Java"));
        repository.save(new Book("Node"));
        repository.save(new Book("Python"));

        System.out.println("\nfindAll()");
        repository.findAll().forEach(x -> System.out.println(x));

        System.out.println("\nfindById(1L)");
        repository.findById(1l).ifPresent(x -> System.out.println(x));

        System.out.println("\nfindByName('Node')");
        repository.findByName("Node").forEach(x -> System.out.println(x));


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

        List<Tweet> tweets;
        try {
            tweets = retrieveTweets(twitter);
        } catch (TwitterException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return tweets;
    }

    private List<Tweet>  retrieveTweets(Twitter twitter) throws TwitterException {

        List<Tweet> tweets = new ArrayList<>();
        List<Status> statuses;// Get last 100 tweets
        statuses = getStatuses(twitter);
        Autolink autolink = new Autolink();
        autolink.setUrlTarget("_");
        SimpleDateFormat sdf = getSimpleDateFormat();
        for (Status status : statuses) {
            Tweet tweet = populateTweet(autolink, sdf, status);
            tweets.add(tweet);
        }
        return tweets;
    }

    private Tweet populateTweet(Autolink autolink, SimpleDateFormat sdf, Status status) {
        Tweet tweet = new Tweet();
        tweet.setTime(sdf.format(status.getCreatedAt()));
        tweet.setUser(status.getUser().getName());
        tweet.setUserLink(autolink.autoLink('@' + status.getUser().getScreenName() +
                " (" + status.getUser().getName() + ")"));
        tweet.setProfileImage(status.getUser().getProfileImageURLHttps());

        tweet.setScreenName(status.getUser().getScreenName());
        //System.out.println(status);

        Status sourceStatus = null;


        if (status.getRetweetedStatus() != null) {
            sourceStatus = status.getRetweetedStatus();
            tweet.setRetweetedBy(autolink.autoLink('@' + status.getUser().getScreenName()) + " Retweeted");
            tweet.setUser(sourceStatus.getUser().getName());
            tweet.setUserLink(autolink.autoLink('@' + sourceStatus.getUser().getScreenName() +
                    " (" + sourceStatus.getUser().getName() + ")"));
            tweet.setProfileImage(sourceStatus.getUser().getProfileImageURLHttps());

        } else if (status.getQuotedStatus() != null) {
            sourceStatus = status.getQuotedStatus();
            tweet.setQuotedText(autolink.autoLink(status.getText()));
            tweet.setQuotedBy(autolink.autoLink('@' + status.getUser().getScreenName()) + " Quoted");
            tweet.setUser(sourceStatus.getUser().getName());
            tweet.setUserLink(autolink.autoLink('@' + sourceStatus.getUser().getScreenName() +
                    " (" + sourceStatus.getUser().getName() + ")"));
            tweet.setProfileImage(sourceStatus.getUser().getProfileImageURLHttps());

        } else {
            sourceStatus = status;
        }

        tweet.setText(sourceStatus.getText());
        if ((sourceStatus.getMediaEntities() != null) && (sourceStatus.getMediaEntities().length >= 1)) {
//            System.out.println(sourceStatusDescription + "Image");
//            System.out.println(sourceStatus.getMediaEntities()[0].getMediaURL());
//            System.out.println(sourceStatus.getMediaEntities()[0].getType());

            tweet.setImage(sourceStatus.getMediaEntities()[0].getMediaURL());
            tweet.setImageType(sourceStatus.getMediaEntities()[0].getType());
        }

        if ((sourceStatus.getURLEntities() != null) && (sourceStatus.getURLEntities().length >= 1)) {
//            System.out.println(sourceStatusDescription + "Url");
//            System.out.println(sourceStatus.getURLEntities()[0].getDisplayURL());
//            System.out.println(sourceStatus.getURLEntities()[0].getExpandedURL());

            tweet.setDisplayUrl(sourceStatus.getURLEntities()[0].getDisplayURL());
            tweet.setExpandedUrl(sourceStatus.getURLEntities()[0].getExpandedURL());

        }

        tweet.setTextLink(autolink.autoLink(tweet.getText()));
        return tweet;
    }

    private List<Status> getStatuses(Twitter twitter) throws TwitterException {
        List<Status> statuses;
        statuses = twitter.getHomeTimeline();
        statuses.addAll(twitter.getHomeTimeline(new Paging(2)));
        statuses.addAll(twitter.getHomeTimeline(new Paging(3)));
        statuses.addAll(twitter.getHomeTimeline(new Paging(4)));
        statuses.addAll(twitter.getHomeTimeline(new Paging(5)));
        return statuses;
    }

    private SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Australia/NSW"));
        return sdf;
    }

}
