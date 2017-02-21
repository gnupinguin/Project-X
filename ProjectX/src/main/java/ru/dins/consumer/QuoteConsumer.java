package ru.dins.consumer;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.dins.model.quote.Quote;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by gnupinguin on 20.02.17.
 */
public class QuoteConsumer implements ProjectXConsumer {

    private KafkaConsumer<String, Quote> quoteConsumer;

    @Getter
    private String topicName;

    public QuoteConsumer(Properties props, String topicName){
        quoteConsumer = new KafkaConsumer<String, Quote>(props);
        this.topicName = topicName;
        quoteConsumer.subscribe(Arrays.asList(topicName));
    }

    public QuoteConsumer(String consumerPropertiesFilename, String topicName) throws IOException{
        Properties props = new Properties();
        props.load(new FileInputStream(consumerPropertiesFilename));
        quoteConsumer = new KafkaConsumer<String, Quote>(props);
        this.topicName = topicName;
        quoteConsumer.subscribe(Arrays.asList(topicName));
    }

    @Override
    public List<Quote> readQuotesFromQueue() {
        ConsumerRecords<String, Quote> records = quoteConsumer.poll(100);
        List<Quote> result = new ArrayList<Quote>(records.count());
        for (ConsumerRecord<String, Quote> record : records){
            result.add(record.value());
        }
        return result;
    }

    @Override
    public void close() {
        quoteConsumer.close();
    }
}
