package com.examples.NewsLetter;

public class Tweet {

    private String tweetId;
    private String content;

    //standard getters and setters

    @Override
    public String toString() {
        return String.format("Tweet{tweetId='%s', content='%s'}",
                tweetId, content);
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String ID) {
        tweetId = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}