package com.eduonix.projectbackend.service;

import com.eduonix.projectbackend.model.TweetTrend;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TweetTrendService {

    private static final Map<String, TweetTrend> tweetTrendMap = new HashMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("E:HH:mm");
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(TweetTrendService.class);

    private final java.util.TimeZone tz = java.util.TimeZone.getTimeZone("GMT+10");

    public List<TweetTrend> getTweetTrends() {
        Collection<TweetTrend> tweetTrends = tweetTrendMap.values();
        List<TweetTrend> itemList = new ArrayList<>(tweetTrends);
        itemList.sort(Comparator.comparing(TweetTrend::getDateFirstSeen).reversed());
        return itemList;
    }

    @Scheduled(fixedRate = 15000)
    private void getTrends() {

        Date date = new Date();
        dateFormat.setTimeZone(tz);

        log.info("The time is now {}", dateFormat.format(date));
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setTweetModeExtended(true);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();

        Trends trends;
        try {
            trends = twitter.getPlaceTrends(1105779);
        } catch (TwitterException e) {
            e.printStackTrace();
            return;
        }
        Trend[] trendList = trends.getTrends();
        for (Trend trend : trendList) {
            if (!tweetTrendMap.containsKey(trend.getName().toUpperCase())) {

                QueryResult queryResult = null;
                try {
                    queryResult = twitter.search(new Query(trend.getName()));
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                log.info("New Trend {} \ntweet{}", trend.getName(), queryResult.getTweets().get(0).getText());




                TweetTrend tweetTrend = new TweetTrend(
                        dateFormat.format(date),
                        date,
                        dateFormat.format(date),
                        Integer.toString(trend.getTweetVolume()),
                        trend.getName(),
                        trend.getURL(),
                        queryResult.getTweets().get(0).getText());

                tweetTrendMap.put(trend.getName().toUpperCase(), tweetTrend);
            }
        }
    }
}
