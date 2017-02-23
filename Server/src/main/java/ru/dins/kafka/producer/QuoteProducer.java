package ru.dins.kafka.producer;

import lombok.Getter;
import lombok.NonNull;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.dins.model.quote.Quote;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by dins on 16.02.17.
 */
public class QuoteProducer implements ProjectXProducer
{
    private AtomicLong key = new AtomicLong(0);
    private KafkaProducer<String, Quote> quoteProducer;

    @Getter @NonNull
    private String topicName;

    public QuoteProducer(Properties props, String topicName){
        quoteProducer = new KafkaProducer<>(props);
        this.topicName = topicName;
    }

    public QuoteProducer(String propertiesFilename, String topicName) throws IOException{
        Properties props = new Properties();
        props.load(new FileInputStream(propertiesFilename));
        quoteProducer = new KafkaProducer<>(props);
        this.topicName = topicName;
    }

    @Override
    public void addQuoteInQueue(Quote quote){
        quoteProducer.send(new ProducerRecord<>(getTopicName(), Long.toString(key.getAndIncrement()), quote));

    }

    @Override
    public void close() {
        quoteProducer.close();
    }
}
