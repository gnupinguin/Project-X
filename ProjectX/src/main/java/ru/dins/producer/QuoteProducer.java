package ru.dins.producer;
import lombok.Getter;
import org.apache.kafka.clients.producer.*;

import java.io.*;
import java.util.Properties;

import ru.dins.model.quote.Quote;

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

    public void addQuoteInQueue(Quote quote){
        quoteProducer.send(new ProducerRecord<String, Quote>(getTopicName(), Long.toString(incrementKey()), quote));
    }

    public QuoteProducer(KafkaProducer<String, Quote> quoteProducer, String topicName){
        this.quoteProducer = quoteProducer;
        this.topicName = topicName;
    }

    public void close() {
        quoteProducer.close();
    }
}
