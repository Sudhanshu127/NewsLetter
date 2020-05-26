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

public class FetchAPI  implements Runnable{
    private static Logger logger = null;
    private final int queryDate;

    FetchAPI(int queryDate){
        logger = LogManager.getLogger(FetchAPI.class);
        this.queryDate = queryDate;
    }
    public static void main() throws IOException{

        String url = "https://api.newsriver.io/v2/search?query=text%3AHey&sortBy=_score&sortOrder=DESC&limit=15";


        logger.trace("Building request");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        request.addHeader("Authorization", Tokens.getNewsriverToken());

        logger.trace("Getting response");
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader rd = null;
        if (response != null) {
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        }

        logger.trace("Converting to string");
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"response\":");
        String line = null;
        while (true) {
            try {
                if (rd != null && (line = rd.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }
        sb.append('}');

        logger.trace("Converting to json array");
        JSONObject json = null;
        try {
            json = new JSONObject(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        try {
            if (json != null) {
                jsonArray = json.getJSONArray("response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        logger.trace("Fetching json objects");
        if (jsonArray != null) {
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject currentJson = null;
                try {
                    currentJson = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                logger.trace("Sending jsonTweet to producer");
                if (currentJson != null) {
                    HelloProducer producer = new HelloProducer("test", currentJson.toString());
                }
            }
        }
    }

    public void run() {
        System.out.println(Thread.currentThread().getName()+" (Start)");
        try {
            main();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" (End)");
    }
}
