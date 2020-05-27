package com.examples.NewsLetter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsLetterConsume {
    public static int consumerThreads = 5;
    public static int consumerProcesses = 5;

    public static void main(String[] args) {


        ExecutorService consumer = Executors.newFixedThreadPool(consumerThreads);
        for(int i=0; i<consumerProcesses; i++)
        {
            consumer.execute(new HelloConsumer("Consumer" + i));
        }
        consumer.shutdown();
        while (!consumer.isTerminated()) {  }
        System.out.println("Finished all consumer threads");
    }
}
