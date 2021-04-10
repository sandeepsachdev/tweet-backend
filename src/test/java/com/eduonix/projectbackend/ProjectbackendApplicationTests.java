package com.eduonix.projectbackend;

import com.eduonix.projectbackend.model.TwitterTrend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectbackendApplicationTests {

	@Test
	public void contextLoads() throws TwitterException, InterruptedException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setTweetModeExtended(true);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		Map<String, Integer> trendSet = new HashMap<String, Integer>();
		while(true) {
			Trends trends = twitter.getPlaceTrends(1105779);
			Trend[] trendList = trends.getTrends();
			//System.out.println("got " + Long.toString(trendList.length) + " new results");


			List<TwitterTrend> twitterTrends = new ArrayList<>();
			for (int i = 0; i < trendList.length; i++) {
				Trend trend = trendList[i];

				if (trend.getTweetVolume() != -1 && !trendSet.containsKey(trend.getName().toUpperCase())) {
					System.out.println("NEW " + trend.getName() + " " + trend.getTweetVolume());
				}

				TwitterTrend twitterTrend = new TwitterTrend();
				twitterTrend.setName(trend.getName());
				twitterTrend.setTotal(trend.getTweetVolume());
				twitterTrend.setUrl(trend.getURL());
				if (trend.getTweetVolume() != -1) {
					if (trendSet.containsKey(trend.getName().toUpperCase())) {
						twitterTrend.setIncrease(trend.getTweetVolume() - trendSet.get(trend.getName().toUpperCase()));
					} else {
						twitterTrend.setIncrease(trend.getTweetVolume());
					}
					trendSet.put(trend.getName().toUpperCase(), trend.getTweetVolume());
					twitterTrends.add(twitterTrend);
				}
			}

			Collections.sort(twitterTrends, (a,b) -> Integer.compare(a.getIncrease(), b.getIncrease()));
			String timeStamp = new SimpleDateFormat("HH:mm ").format(Calendar.getInstance().getTime());

			System.out.println("-------------------------------");
			System.out.println("Top " + twitterTrends.size() +  " trends at " + timeStamp );
			System.out.println("-------------------------------");

			for (int i = 0; i < twitterTrends.size(); i++) {
				TwitterTrend twitterTrend =  twitterTrends.get(i);
				System.out.println(String.format("%02d", twitterTrends.size() - i) + " " + twitterTrend.getName() + " " +
						twitterTrend.getUrl() + " " +
						twitterTrend.getTotal() + "+" +
						twitterTrend.getIncrease());
			}



			Thread.sleep(1000 * 120);

		}
	}

}
