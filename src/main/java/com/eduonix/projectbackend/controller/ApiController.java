package com.eduonix.projectbackend.controller;


import com.apptastic.rssreader.Item;
import com.eduonix.projectbackend.model.Article;
import com.eduonix.projectbackend.model.Response;
import com.eduonix.projectbackend.model.SmhItem;
import com.eduonix.projectbackend.model.Tweet;
import com.eduonix.projectbackend.service.SmhRssService;
import com.eduonix.projectbackend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ApiController {

	@Value("${newsApiKey}")
	String newsApiKey;

	@Autowired
	TweetService tweetService;

	@Autowired
	SmhRssService smhRssService;

	@Autowired
	RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@RequestMapping(value="/getTweets",produces="application/json")
	public List<Tweet> getTweets() {
		return tweetService.getTweets();
	}

	@RequestMapping(value="/getSmhRss",produces="application/json")
	public List<SmhItem> getSmhRss() {
		return smhRssService.getRss();
	}

	@RequestMapping(value="/getNewsApiRss",produces="application/json")
	public List<Article> getNewsApiRss() {
		return restTemplate.getForObject(
				"http://newsapi.org/v2/everything?pageSize=100&language=en&sortBy=publishedAt&q=covid&from=2020-07-16&apiKey=" + newsApiKey, Response.class).getArticles();
	}

}
