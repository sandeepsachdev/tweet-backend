/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eduonix.projectbackend.service;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.exit;

@Component
public class ScheduledTasks {

	private static final Map<String, Item> articlesMap = new HashMap<String,Item>();
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedRate = 3600000)
	private void printSmhFeed() {

		log.info("The time is now {}", dateFormat.format(new Date()));
		RssReader reader = new RssReader();
		Stream<Item> rssFeed = null;
		List<Item> articles = new ArrayList<>();

		{
			try {
				articles.addAll(reader.read("https://www.smh.com.au/rss/feed.xml").collect(Collectors.toList()));
				articles.addAll(reader.read("https://www.smh.com.au/rss/politics/federal.xml").collect(Collectors.toList()));
				articles.addAll(reader.read("https://www.smh.com.au/rss/national/nsw.xml").collect(Collectors.toList()));
				articles.addAll(reader.read("https://www.smh.com.au/rss/world.xml").collect(Collectors.toList()));
				articles.addAll(reader.read("https://www.smh.com.au/rss/national.xml").collect(Collectors.toList()));
				articles.addAll(reader.read("https://www.smh.com.au/rss/business.xml").collect(Collectors.toList()));
				articles.addAll(reader.read("https://www.smh.com.au/rss/culture.xml").collect(Collectors.toList()));
				articles.addAll(reader.read("https://www.smh.com.au/rss/lifestyle.xml").collect(Collectors.toList()));
			} catch (IOException e) {
				e.printStackTrace();
				exit(0);
			}
		}

		for (Item item: articles) {
			if (item.getGuid().isPresent() && !articlesMap.containsKey(item.getGuid().get())) {
				articlesMap.put(item.getGuid().get(), item);
				System.out.println(item.getGuid().get() + " " + item.getLink().get());
			}
		}
	}


}
