package ru.dins.kafka.consumer;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.dins.web.model.quote.Quote;

import java.io.IOException;
import java.util.*;

/**
 * Created by gnupinguin on 20.02.17.
 */
@Service  @NoArgsConstructor
public class QuoteConsumerImpl implements QuoteConsumer {
    @NonNull
    private Consumer<String, Quote> consumer;

    private final int DEFAULT_PARTITION = 0;

    private TopicPartition topicPartition;

    public QuoteConsumerImpl(String consumerPropertiesFilename, TopicPartition topicPartition) throws IOException{
        Properties props = new Properties();
        props.load(new ClassPathResource(consumerPropertiesFilename).getInputStream());
        consumer = new KafkaConsumer<>(props);
        consumer.assign(Arrays.asList(topicPartition));
        this.topicPartition = topicPartition;
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
            return new QuoteConsumerImpl(filename, new TopicPartition(topic, DEFAULT_PARTITION));
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
    public Map<Long, Quote> readQuotesFromQueueWithOffsets() {
        ConsumerRecords<String, Quote> records = consumer.poll(100);
        Map<Long, Quote> result = new HashMap<>(records.count());
        for (ConsumerRecord<String, Quote> record : records){
            result.put(record.offset(), record.value());
        }
        return result;
    }

    @Override
    public void seek(long offset) {
        consumer.seek(topicPartition, offset);
    }

    @Override
    public void commit(long offset) {
        consumer.commitSync(Collections.singletonMap(topicPartition, new OffsetAndMetadata(offset)));
    }

    @Override
    public void close() {
        consumer.close();
    }
}
