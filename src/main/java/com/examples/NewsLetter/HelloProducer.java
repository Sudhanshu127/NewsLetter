package com.examples.NewsLetter;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class HelloProducer implements Runnable{
    private static final Logger logger = LogManager.getLogger(HelloProducer.class);
    private static Properties props = null;
    private String topic = "test";
    private String tweet = "{}";
    HelloProducer(String topic, String tweet){
        logger.info("Starting HelloProducer...");
        logger.trace("Creating Kafka Producer...");
        props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "HelloProducer");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.topic = topic;
        this.tweet = tweet;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName()+" (Start)");
        produceTweet();
        System.out.println(Thread.currentThread().getName()+" (End)");
    }

    void produceTweet() {
        try (KafkaProducer<Integer, String> producer = new KafkaProducer<>(props)) {
            logger.trace("Start sending tweet...");
                producer.send(new ProducerRecord<>(this.topic, this.tweet));
        } catch (KafkaException e) {
            logger.error("Exception occurred - Check log for more details.\n" + e.getMessage());
            System.exit(-1);
        } finally {
            logger.info("Finished HelloProducer - Closing Kafka Producer.");
        }

    }
}
