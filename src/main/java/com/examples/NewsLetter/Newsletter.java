package com.examples.NewsLetter;

import org.json.JSONException;

import java.io.IOException;

public class Newsletter {
    public static void main(String[] args) {
        Thread consumerThread = new Thread(() -> {
            try {
                HelloConsumer.main();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread fetchThread = new Thread(()-> {
            try {
                FetchAPI.main();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
        consumerThread.start();
        fetchThread.start();
    }
}
