package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import quote.Quote;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;


/**
 * Created by gnupinguin on 16.02.17.
 */
public class LocalTopicConsumer {
    private static final String REPLICA_TOPIC = "quote-replica";
    private static final String LOCAL_TOPIC = "quote-local";


    public static void main(String[] args) throws Exception {
        String producerInnerReplicaPropertiesFilename = "target/config/local-consumer/producerInnerReplica.properties";
        String producerOuterReplicaPropertiesFilename = "target/config/local-consumer/producerOuterReplica.properties";
        String consumerPropertiesFileName = "target/config/local-consumer/consumerProperties.properties";

        Properties producerInnerReplicaProperties = new Properties();
        producerInnerReplicaProperties.load(new FileInputStream(producerInnerReplicaPropertiesFilename));

        Properties producerOuterReplicaProperties = new Properties();
        producerOuterReplicaProperties.load(new FileInputStream(producerOuterReplicaPropertiesFilename));

        Properties consumerProperties = new Properties();
        consumerProperties.load(new FileInputStream(consumerPropertiesFileName));

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
