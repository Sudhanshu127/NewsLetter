package com.examples.NewsLetter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsLetterProduce {
    public static int fetchThreads = 10;
    public static int daysToDecrement = -1;
    public static Date date = new Date();
    public static int dataDays = -365*5;

    public static void main(String[] args) {

        Thread fetchThread = new Thread(()-> {
            ExecutorService fetch = Executors.newFixedThreadPool(fetchThreads);
            Calendar cal = Calendar.getInstance();
            cal.setTime ( date );

            Date finalDate = (Date) date.clone();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime ( finalDate );
            cal2.add(Calendar.DATE,dataDays);
            finalDate = cal2.getTime();

            while(true) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                FetchAPI fetchAPI = new FetchAPI(dateFormat.format(date));
                fetch.execute(fetchAPI);
                cal.add(Calendar.DATE, daysToDecrement);
                date = cal.getTime();
                try {
                    Thread.sleep(4001);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(date.equals(finalDate))
                    break;
            }
            fetch.shutdown();
            while (!fetch.isTerminated()) {
            }
            System.out.println("Finished all fetch threads");
        });
        fetchThread.start();
    }
}
