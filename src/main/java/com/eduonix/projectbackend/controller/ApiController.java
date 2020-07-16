package com.eduonix.projectbackend.controller;


import com.apptastic.rssreader.Item;
import com.eduonix.projectbackend.model.Tweet;
import com.eduonix.projectbackend.service.SmhRssService;
import com.eduonix.projectbackend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

	@Autowired
	TweetService tweetService;

	@Autowired
	SmhRssService smhRssService;

	@RequestMapping(value="/getTweets",produces="application/json")
	public List<Tweet> getTweets() {
		return tweetService.getTweets();
	}

	@RequestMapping(value="/getSmhRss",produces="application/json")
	public List<Item> getSmhRss() {
		return smhRssService.getRss();
	}



}
