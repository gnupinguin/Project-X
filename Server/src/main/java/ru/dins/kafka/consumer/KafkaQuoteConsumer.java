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
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

/**
 * Created by gnupinguin on 20.02.17.
 */
@Service  @NoArgsConstructor
public class KafkaQuoteConsumer implements QuoteConsumer {
    @NonNull
    private Consumer<String, Quote> consumer;

    private final int DEFAULT_PARTITION = 0;

    private TopicPartition topicPartition;

    private String host;
    private int port;

    public KafkaQuoteConsumer(String consumerPropertiesFilename, TopicPartition topicPartition, String host, int port) throws IOException{
        Properties props = new Properties();
        props.load(new ClassPathResource(consumerPropertiesFilename).getInputStream());
        consumer = new KafkaConsumer<>(props);
        consumer.assign(Arrays.asList(topicPartition));
        this.topicPartition = topicPartition;
        this.host = host;
        this.port = port;
    }

    @Bean
    public KafkaQuoteConsumer innerMainPartitionLocalTopicConsumerFromFile(
            @Value("${kafka.inner-local-topic-consumer-path}") String filename,
            @Value("${kafka.local-topic-name}") String topic,
            @Value("${kafka.main-partition-local-topic}")Integer partition,
            @Value("${kafka.local-host}") String host,
            @Value("${kafka.local-port}" )int port){
        try{
            return new KafkaQuoteConsumer(filename, new TopicPartition(topic, partition), host, port);
        } catch (IOException e){
            System.err.println("Error initialization inner local topic mainPartitionConsumer");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    public KafkaQuoteConsumer innerReservePartitionLocalTopicConsumerFromFile(
            @Value("${kafka.inner-local-topic-consumer-path}") String filename,
            @Value("${kafka.local-topic-name}") String topic,
            @Value("${kafka.reserve-partition-local-topic}")Integer partition,
            @Value("${kafka.local-host}") String host,
            @Value("${kafka.local-port}" )int port){
        try{
            return new KafkaQuoteConsumer(filename, new TopicPartition(topic, partition), host, port);
        } catch (IOException e){
            System.err.println("Error initialization inner local topic mainPartitionConsumer");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    public KafkaQuoteConsumer outerReplicaTopicConsumerFromFile(
            @Value("${kafka.outer-replica-topic-consumer-path}")String filename,
            @Value("${kafka.replica-topic-name}")String topic,
            @Value("${kafka.remote-host}") String host,
            @Value("${kafka.remote-port}" )int port){
        try{
            return new KafkaQuoteConsumer(filename, new TopicPartition(topic, DEFAULT_PARTITION), host, port);
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
    public boolean availableConnection() {
        try{
            new Socket(InetAddress.getByName(host), port).close();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public void close() {
        consumer.close();
    }
}
