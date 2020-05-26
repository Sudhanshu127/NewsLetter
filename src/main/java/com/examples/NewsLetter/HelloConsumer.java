package com.examples.NewsLetter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class HelloConsumer implements Runnable{
    private static final Logger logger = LogManager.getLogger(HelloConsumer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final String name;
    public HelloConsumer(String s) {
        this.name = s;
    }

    public void run(){
        System.out.println(Thread.currentThread().getName()+" (Start)");
        try {
            main();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" (End)");
    }


    public void main() throws IOException {
//        System.out.println("Starting "+ name +"...");
        logger.info("Starting "+ name +"...");
        logger.trace("Creating Kafka Consumer...");
        Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, name);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
//        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 100);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-tutorial3");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        logger.trace("Sending connection request to elasticsearch");
        ElasticSearchQuery.makeConnection();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            logger.trace("Start reading messages from test...");
            consumer.subscribe(Collections.singleton("test"));
            int i=1;
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {

                    if(record.value().charAt(0) != '{')
                        continue;
                    logger.trace("Creating tweet");
                    Tweet tweet = objectMapper.readValue(record.value(),Tweet.class);
                    System.out.println(name + " is inserting tweet " + i);
                    i++;
                    Tweet myTweet = ElasticSearchQuery.insertTweet(tweet);
//                    System.out.println(ElasticSearchQuery.getTweetById(myTweet.getTweetId()));
                }
            }
        } catch (KafkaException e) {
            logger.error("Exception occurred - Check log for more details.\n" + e.getMessage());
            System.exit(-1);
        } finally {
            logger.info("Finished HelloConsumer - Closing Kafka Consumer.");
        }
        logger.trace("Closing connection with elasticsearch");
        ElasticSearchQuery.closeConnection();
    }
}
