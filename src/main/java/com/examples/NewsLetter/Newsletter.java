package com.examples.NewsLetter;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Newsletter {
    public static int fetchThreads = 5;
    public static int consumerThreads = 5;
    public static int fetchProcesses = 5;
    public static int consumerProcesses = 5;
    public static void main(String[] args) {

        fetchData();

        ExecutorService consumer = Executors.newFixedThreadPool(consumerThreads);
        for(int i=0; i<consumerProcesses; i++)
        {
            consumer.execute(new HelloConsumer("Consumer" + i));
        }
        consumer.shutdown();
        while (!consumer.isTerminated()) {  }
        System.out.println("Finished all consumer threads");

    }

    static void fetchData(){
        Thread fetchThread = new Thread(()-> {
            ExecutorService fetch = Executors.newFixedThreadPool(fetchThreads);
            for (int i = 0; i < fetchProcesses; i++) {
                FetchAPI fetchAPI = new FetchAPI(i);
                fetch.execute(fetchAPI);
            }
            fetch.shutdown();
            while (!fetch.isTerminated()) {
            }
            System.out.println("Finished all fetch threads");
        });
        fetchThread.start();
    }
}
