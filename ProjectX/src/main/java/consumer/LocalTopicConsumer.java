package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import quote.Quote;

import java.util.Arrays;
import java.util.Properties;


/**
 * Created by gnupinguin on 16.02.17.
 */
public class LocalTopicConsumer {

    public static void main(String[] args) throws Exception {
        String replicaTopic  = "quote-replica";
        String localTopic = "quote-local";

        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "192.168.62.221:9092");
        producerProperties.put("acks", "all");
        producerProperties.put("retries", 0);
        producerProperties.put("batch.size", 16384);
        producerProperties.put("linger.ms", 1);
        producerProperties.put("buffer.memory", 33554432);
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "quote.QuoteSerializer");

        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "192.168.62.221:9092");
        consumerProperties.put("group.id", "0");
        consumerProperties.put("enable.auto.commit", "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "quote.QuoteDeserializer");

        KafkaConsumer<String, Quote> consumer = new KafkaConsumer<String, Quote>(consumerProperties);
        KafkaProducer<String, Quote> quoteProducer = new KafkaProducer<String, Quote>(producerProperties);

        consumer.subscribe(Arrays.asList(localTopic));

        while (true) {
            ConsumerRecords<String, Quote> records = consumer.poll(100);
            for (ConsumerRecord<String, Quote> record : records){
                quoteProducer.send(new ProducerRecord<String, Quote>(replicaTopic, record.key(), record.value()));
            }

        }

    }
}
