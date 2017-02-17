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
    private static final String REPLICA_TOPIC = "quote-replica";
    private static final String LOCAL_TOPIC = "quote-local";


    public static void main(String[] args) throws Exception {

        Properties producerInnerReplicaProperties = new Properties();
        producerInnerReplicaProperties.put("bootstrap.servers", "192.168.62.221:9092");
        producerInnerReplicaProperties.put("acks", "all");
        producerInnerReplicaProperties.put("retries", 0);
        producerInnerReplicaProperties.put("batch.size", 16384);
        producerInnerReplicaProperties.put("linger.ms", 1);
        producerInnerReplicaProperties.put("buffer.memory", 33554432);
        producerInnerReplicaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerInnerReplicaProperties.put("value.serializer", "quote.QuoteSerializer");

        Properties producerOuterReplicaProperties = new Properties();
        producerOuterReplicaProperties.put("bootstrap.servers", "192.168.62.191:9092");
        producerOuterReplicaProperties.put("acks", "all");
        producerOuterReplicaProperties.put("retries", 0);
        producerOuterReplicaProperties.put("batch.size", 16384);
        producerOuterReplicaProperties.put("linger.ms", 1);
        producerOuterReplicaProperties.put("buffer.memory", 33554432);
        producerOuterReplicaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerOuterReplicaProperties.put("value.serializer", "quote.QuoteSerializer");

        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "192.168.62.221:9092");
        consumerProperties.put("group.id", "0");
        consumerProperties.put("enable.auto.commit", "true");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "quote.QuoteDeserializer");

        KafkaConsumer<String, Quote> consumer = new KafkaConsumer<String, Quote>(consumerProperties);
        KafkaProducer<String, Quote> producerInnerReplica = new KafkaProducer<String, Quote>(producerInnerReplicaProperties);
        KafkaProducer<String, Quote> producerOuterReplica = new KafkaProducer<String, Quote>(producerOuterReplicaProperties);

        consumer.subscribe(Arrays.asList(LOCAL_TOPIC));

        while (true) {
            ConsumerRecords<String, Quote> records = consumer.poll(100);
            for (ConsumerRecord<String, Quote> record : records){
                producerInnerReplica.send(new ProducerRecord<String, Quote>(REPLICA_TOPIC, record.key(), record.value()));
                producerOuterReplica.send(new ProducerRecord<String, Quote>(REPLICA_TOPIC, record.key(), record.value()));
            }

        }

    }
}
