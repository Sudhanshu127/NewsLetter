package com.examples.NewsLetter;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FetchAPI {
    public static void main(String[] args) throws IOException, JSONException {

        String url = "https://api.newsriver.io/v2/search?query=text%3AHey&sortBy=_score&sortOrder=DESC&limit=15";

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

// add authorization header
        request.addHeader("Authorization", Tokens.getNewsriverToken());

        CloseableHttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuilder sb = new StringBuilder();
        sb.append("{ \"response\":");
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        sb.append('}');
        JSONObject json = new JSONObject(sb.toString());
        JSONArray jsonArray = json.getJSONArray("response");
        System.out.println(jsonArray.length());
    }
}
