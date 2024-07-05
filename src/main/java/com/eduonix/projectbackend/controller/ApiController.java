package com.eduonix.projectbackend.controller;


import com.eduonix.projectbackend.model.*;
import com.eduonix.projectbackend.service.SmhRssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ApiController {

	@Value("${newsApiKey}")
	String newsApiKey;

	@Autowired
	SmhRssService smhRssService;

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value="/getSmhRss",produces="application/json")
	public List<SmhItem> getSmhRss() {
		return smhRssService.getRss();
	}

	@RequestMapping(value="/getNewsApiRss",produces="application/json")
	public List<Article> getNewsApiRss() {
		return smhRssService.getNewsApiRss();
	}

}
