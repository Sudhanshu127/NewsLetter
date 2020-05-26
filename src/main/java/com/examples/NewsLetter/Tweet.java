package com.examples.NewsLetter;

public class Tweet {

    private String tweetId;
    private String text;
    private String url;
    private String highlight;
    private double score;
    private String discoverDate;


    //standard getters and setters

    @Override
    public String toString() {
        return String.format("Tweet{\n" +
                             "      tweetId = '%s',\n " +
                             "      url = '%s',\n" +
                             "      highlight = '%s',\n" +
                             "      score = '%f'\n" +
                             "      text = '%s'\n" +
                             "      discoverDate = '%s'\n" +
                             "     }",
                tweetId, url, highlight, score, text, discoverDate);
    }

    public String getTweetId() {
        return tweetId;
    }

    public Tweet setTweetId(String ID) {
        tweetId = ID;
        return this;
    }

    public String getText() {
        return text;
    }

    public Tweet setText(String text) {
        this.text = text;
        return this;
    }
    public String getUrl() {
        return url;
    }

    public Tweet setUrl(String url) {
        this.url = url;
        return this;
    }
    public String getHighlight() {
        return highlight;
    }

    public Tweet setHighlight(String highlight) {
        this.highlight = highlight;
        return this;
    }
    public double getScore() {
        return score;
    }

    public Tweet setScore(double score) {
        this.score = score;
        return this;
    }

    public String getDiscoverDate() {
        return discoverDate;
    }

    public Tweet setDiscoverDate(String discoverDate) {
        this.discoverDate = discoverDate;
        return this;
    }

}