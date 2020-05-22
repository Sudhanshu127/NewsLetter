package com.examples.NewsLetter;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FetchAPI {
    private static final Logger logger = LogManager.getLogger(FetchAPI.class);
    public static void main() throws IOException, JSONException {

        String url = "https://api.newsriver.io/v2/search?query=text%3AHey&sortBy=_score&sortOrder=DESC&limit=15";


        logger.trace("Building request");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("Authorization", Tokens.getNewsriverToken());

        logger.trace("Getting response");
        CloseableHttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        logger.trace("Converting to string");
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"response\":");
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        sb.append('}');

        logger.trace("Converting to json array");
        JSONObject json = new JSONObject(sb.toString());
        JSONArray jsonArray = json.getJSONArray("response");

        logger.trace("Creating a thread poll for producers");
        ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads

        logger.trace("Fetching json objects");
        for(int i = 0; i < jsonArray.length(); i++)
        {
            JSONObject currentJson = jsonArray.getJSONObject(i);
            logger.trace("Sending jsonTweet to producer");
            HelloProducer producer = new HelloProducer("test", currentJson.toString());
            executor.execute(producer);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {  }
        System.out.println("Finished all threads");
    }
}
