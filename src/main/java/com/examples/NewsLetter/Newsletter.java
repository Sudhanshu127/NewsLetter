package com.examples.NewsLetter;

import org.json.JSONException;

import java.io.IOException;

public class Newsletter {
    public static void main(String[] args) {
        Thread fetchThread = new Thread(()-> {
            try {
                FetchAPI.main();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
        fetchThread.start();
        for(int i=0; i<5; i++)
        {
            consumer("Consumer" + i);
        }
    }

    public static void consumer(String name){
        Thread consumerThread = new Thread(() -> {
            try {
                HelloConsumer.main(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        consumerThread.start();

    }
}
