package com.eduonix.projectbackend.service;

import com.eduonix.projectbackend.model.Book;
import com.eduonix.projectbackend.model.SmhItem;
import com.eduonix.projectbackend.model.Tweet;
import com.eduonix.projectbackend.model.TweetTrend;
import com.eduonix.projectbackend.repository.BookRepository;
import com.twitter.twittertext.Autolink;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.TimeZone;

@Component
public class TweetTrendService {

    private static final Map<String, TweetTrend> tweetTrendMap = new HashMap<String, TweetTrend>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("E HH:mm");
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TweetTrendService.class);

    public List<TweetTrend> getTweetTrends() {
        Collection<TweetTrend> tweetTrends = tweetTrendMap.values();
        List<TweetTrend> itemList = new ArrayList<>(tweetTrends);
        itemList.sort(Comparator.comparing(TweetTrend::getDateFirstSeen).reversed());
        return itemList;
    }

    @Scheduled(fixedRate = 120000)
    private void getTrends() {

        log.info("The time is now {}", dateFormat.format(new Date()));
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setTweetModeExtended(true);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        Trends trends = null;
        try {
            trends = twitter.getPlaceTrends(1105779);
        } catch (TwitterException e) {
            e.printStackTrace();
            return;
        }
        Trend[] trendList = trends.getTrends();
        for (Trend trend : trendList) {
            if (!tweetTrendMap.containsKey(trend.getName().toUpperCase())) {
                log.info("New Trend {}", trend.getName());
                TweetTrend tweetTrend = new TweetTrend(
                        dateFormat.format(new Date()),
                        new Date(),
                        dateFormat.format(new Date()),
                        Integer.toString(trend.getTweetVolume()),
                        trend.getName(),
                        trend.getURL());

                tweetTrendMap.put(trend.getName().toUpperCase(), tweetTrend);
            }
        }
    }
}
