package com.examples.NewsLetter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class HelloConsumer {
    private static final Logger logger = LogManager.getLogger(HelloConsumer.class);

    public static void main(String[] args) {
        logger.info("Starting HelloConsumer...");
        logger.trace("Creating Kafka Consumer...");
        Properties props = new Properties();
//        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "HelloConsumer");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);
//        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 100);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-tutorial2");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            logger.trace("Start reading messages from test...");
            consumer.subscribe(Collections.singleton("test"));
            Integer temp = 10;
            while (temp > 0) {
                temp = temp -1;
                logger.trace("Time remaining :- " + temp.toString());
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records)
                    logger.trace(record.offset() + ": " + record.value());
            }
        } catch (KafkaException e) {
            logger.error("Exception occurred - Check log for more details.\n" + e.getMessage());
            System.exit(-1);
        } finally {
            logger.info("Finished HelloProducer - Closing Kafka Consumer.");
        }
    }
}
