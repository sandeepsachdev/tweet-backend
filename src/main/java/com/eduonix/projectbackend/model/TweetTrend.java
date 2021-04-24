package com.eduonix.projectbackend.model;

import java.util.Date;

public class TweetTrend {

    String timeFirstSeen;
    Date dateFirstSeen;
    String timelastSeen;
    String count;
    String name;
    String url;
    String tweetText;

    public TweetTrend(String timeFirstSeen, Date dateFirstSeen, String timelastSeen, String count, String name, String url, String tweetText) {
        this.timeFirstSeen = timeFirstSeen;
        this.dateFirstSeen = dateFirstSeen;
        this.timelastSeen = timelastSeen;
        this.count = count;
        this.name = name;
        this.url = url;
        this.tweetText = tweetText;
    }

    public String getTimeFirstSeen() {
        return timeFirstSeen;
    }

    public void setTimeFirstSeen(String timeFirstSeen) {
        this.timeFirstSeen = timeFirstSeen;
    }

    public String getTimelastSeen() {
        return timelastSeen;
    }

    public void setTimelastSeen(String timelastSeen) {
        this.timelastSeen = timelastSeen;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateFirstSeen() {
        return dateFirstSeen;
    }

    public void setDateFirstSeen(Date dateFirstSeen) {
        this.dateFirstSeen = dateFirstSeen;
    }
}
