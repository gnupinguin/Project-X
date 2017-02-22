package ru.dins.kafka.producer;

import lombok.Getter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import ru.dins.model.quote.Quote;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by dins on 16.02.17.
 */
public class QuoteProducer implements ProjectXProducer
{
    private long key = 0;

    @Getter
    private String topicName;

    private long incrementKey(){return key++;}

    private KafkaProducer<String, Quote> quoteProducer;

    public QuoteProducer(Properties props, String topicName){
        quoteProducer = new KafkaProducer<String, Quote>(props);
        this.topicName = topicName;
    }

    public QuoteProducer(String propertiesFilename, String topicName) throws IOException{
        Properties props = new Properties();
        props.load(new FileInputStream(propertiesFilename));
        quoteProducer = new KafkaProducer<String, Quote>(props);
        this.topicName = topicName;
    }

    @Override
    public void addQuoteInQueue(Quote quote){
         quoteProducer.send(new ProducerRecord<String, Quote>(getTopicName(), Long.toString(incrementKey()), quote));
    }

    @Override
    public void close() {
        quoteProducer.close();
    }
}
