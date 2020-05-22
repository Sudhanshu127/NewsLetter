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

public class HelloProducer {
    private static final Logger logger = LogManager.getLogger(HelloProducer.class);
    private static HelloProducer instance = null;
    private static Properties props = null;
    private static final String topic = "test";
    private HelloProducer(){
        logger.info("Starting HelloProducer...");
        logger.trace("Creating Kafka Producer...");
        props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "HelloProducer");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    }

    public static HelloProducer getInstance(){
        logger.trace("Fetching instance");
        if(instance == null)
            instance = new HelloProducer();
        return instance;
    }

    void produceTweet(String tweet) {
        try (KafkaProducer<Integer, String> producer = new KafkaProducer<>(props)) {
            logger.trace("Start sending tweet...");
                producer.send(new ProducerRecord<>(topic, tweet));
        } catch (KafkaException e) {
            logger.error("Exception occurred - Check log for more details.\n" + e.getMessage());
            System.exit(-1);
        } finally {
            logger.info("Finished HelloProducer - Closing Kafka Producer.");
        }

    }
}
