package com.getAcessToken.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChildrenData {
    @Id
    private String id;
    private String author_fullname;
    private String title;
    @JsonProperty("subreddit_subscribers")
    private Integer subredditSubscribers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor_fullname() {
        return author_fullname;
    }

    public void setAuthor_fullname(String author_fullname) {
        this.author_fullname = author_fullname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSubredditSubscribers() {
        return subredditSubscribers;
    }

    public void setSubredditSubscribers(Integer subredditSubscribers) {
        this.subredditSubscribers = subredditSubscribers;
    }
}
