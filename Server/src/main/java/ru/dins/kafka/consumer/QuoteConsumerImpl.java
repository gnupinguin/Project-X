package ru.dins.kafka.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.dins.kafka.producer.QuoteProducerImpl;
import ru.dins.web.model.quote.Quote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by gnupinguin on 20.02.17.
 */
@Service @Data @NoArgsConstructor
public class QuoteConsumerImpl implements QuoteConsumer {
    @NonNull
    private Consumer<String, Quote> consumer;

    public QuoteConsumerImpl(String consumerPropertiesFilename, String topicName) throws IOException{
        Properties props = new Properties();
        props.load(new ClassPathResource(consumerPropertiesFilename).getInputStream());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topicName));
    }

    public QuoteConsumerImpl(String consumerPropertiesFilename, TopicPartition topicPartition) throws IOException{
        Properties props = new Properties();
        props.load(new ClassPathResource(consumerPropertiesFilename).getInputStream());
        consumer = new KafkaConsumer<>(props);
        consumer.assign(Arrays.asList(topicPartition));
    }

    @Bean
    public QuoteConsumerImpl innerMainPartitionLocalTopicConsumerFromFile(
            @Value("${kafka.inner-local-topic-consumer-path}") String filename,
            @Value("${kafka.local-topic-name}") String topic,
            @Value("${kafka.main-partition-local-topic}")Integer partition){
        try{
            return new QuoteConsumerImpl(filename, new TopicPartition(topic, partition));
        } catch (IOException e){
            System.err.println("Error initialization inner local topic mainPartitionConsumer");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    public QuoteConsumerImpl innerReservePartitionLocalTopicConsumerFromFile(
            @Value("${kafka.inner-local-topic-consumer-path}") String filename,
            @Value("${kafka.local-topic-name}") String topic,
            @Value("${kafka.reserve-partition-local-topic}")Integer partition){
        try{
            return new QuoteConsumerImpl(filename, new TopicPartition(topic, partition));
        } catch (IOException e){
            System.err.println("Error initialization inner local topic mainPartitionConsumer");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    public QuoteConsumerImpl outerReplicaTopicConsumerFromFile(
            @Value("${kafka.outer-replica-topic-consumer-path}")String filename,
            @Value("${kafka.replica-topic-name}")String topic){
        try{
            return new QuoteConsumerImpl(filename, topic);
        } catch (IOException e){
            System.err.println("Error initialization outer replica topic mainPartitionConsumer");
            System.err.println(e);
        }
        return null;
    }

    @Override
    public List<Quote> readQuotesFromQueue() {
        ConsumerRecords<String, Quote> records = consumer.poll(100);
        List<Quote> result = new ArrayList<>(records.count());
        for (ConsumerRecord<String, Quote> record : records){
            result.add(record.value());
        }
        return result;
    }

    @Override
    public void close() {
        consumer.close();
    }
}