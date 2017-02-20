package ru.dins.consumer;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.dins.model.quote.Quote;
import ru.dins.producer.ImplementProducer;
import ru.dins.producer.ProjectXProducer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;


/**
 * Created by gnupinguin on 16.02.17.
 */
public class LocalTopicConsumer implements ProjectXConsumer {
//    private static final String REPLICA_TOPIC = "ru.dins.quote-replica";
//    private static final String LOCAL_TOPIC = "ru.dins.quote-local";


//    public static void main(String[] args) throws Exception {
//        String producerInnerReplicaPropertiesFilename = "target/config/local-ru.dins.consumer/producerInnerReplica.properties";
//        String producerOuterReplicaPropertiesFilename = "target/config/local-ru.dins.consumer/producerOuterReplica.properties";
//        String consumerPropertiesFileName = "target/config/local-ru.dins.consumer/consumerProperties.properties";
//
//        Properties producerInnerReplicaProperties = new Properties();
//        producerInnerReplicaProperties.load(new FileInputStream(producerInnerReplicaPropertiesFilename));
//
//        Properties producerOuterReplicaProperties = new Properties();
//        producerOuterReplicaProperties.load(new FileInputStream(producerOuterReplicaPropertiesFilename));
//
//        Properties consumerProperties = new Properties();
//        consumerProperties.load(new FileInputStream(consumerPropertiesFileName));
//
//        KafkaConsumer<String, Quote> consumer = new KafkaConsumer<String, Quote>(consumerProperties);
//        KafkaProducer<String, Quote> producerInnerReplica = new KafkaProducer<String, Quote>(producerInnerReplicaProperties);
//        KafkaProducer<String, Quote> producerOuterReplica = new KafkaProducer<String, Quote>(producerOuterReplicaProperties);
//
//        consumer.subscribe(Arrays.asList(LOCAL_TOPIC));
//        while (true) {
//            ConsumerRecords<String, Quote> records = consumer.poll(100);
//            for (ConsumerRecord<String, Quote> record : records){
//                producerInnerReplica.send(new ProducerRecord<String, Quote>(REPLICA_TOPIC, record.key(), record.value()));
//                producerOuterReplica.send(new ProducerRecord<String, Quote>(REPLICA_TOPIC, record.key(), record.value()));
//            }
//
//        }
//
//    }

    private ProjectXProducer producerInnerReplica;
    private ProjectXProducer producerOuterReplica;
    private KafkaConsumer<String, Quote> consumer;

    @Getter
    private String localTopicName;

    public LocalTopicConsumer(Properties producerInnerReplicaProperties, Properties producerOuterReplicaProperties,
                              Properties consumerProperties, String replicaTopicName, String localTopicName){

        producerInnerReplica = new ImplementProducer(producerInnerReplicaProperties, replicaTopicName);
        producerOuterReplica = new ImplementProducer(producerOuterReplicaProperties, replicaTopicName);
        consumer = new KafkaConsumer<String, Quote>(consumerProperties);

    }

    public void readQuoteFromQueue() {

    }

    public void close() {
        try{
            producerInnerReplica.close();
            producerOuterReplica.close();
            consumer.close();
        } catch (IOException e){

        }

    }
}
