package ru.dins.consumer;

import ru.dins.model.quote.Quote;
import ru.dins.producer.QuoteProducer;
import ru.dins.producer.ProjectXProducer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


/**
 * Created by gnupinguin on 16.02.17.
 */
public class LocalTopicConsumerProducer implements ProjectXConsumer, ProjectXProducer{


    private ProjectXProducer producerInnerReplica;
    private ProjectXProducer producerOuterReplica;
    private ProjectXConsumer consumer;


    public LocalTopicConsumerProducer(Properties producerInnerReplicaProperties, Properties producerOuterReplicaProperties,
                              Properties consumerProperties, String replicaTopicName, String localTopicName){

        producerInnerReplica = new QuoteProducer(producerInnerReplicaProperties, replicaTopicName);
        producerOuterReplica = new QuoteProducer(producerOuterReplicaProperties, replicaTopicName);
        consumer = new QuoteConsumer(consumerProperties, localTopicName);


    }

    public LocalTopicConsumerProducer(String producerInnerReplicaPropertiesFilename,
                              String producerOuterReplicaPropertiesFilename,
                              String consumerPropertiesFileName,
                              String replicaTopicName,
                              String localTopicName) throws IOException{

        producerInnerReplica = new QuoteProducer(producerInnerReplicaPropertiesFilename, replicaTopicName);
        producerOuterReplica = new QuoteProducer(producerOuterReplicaPropertiesFilename, replicaTopicName);
        consumer = new QuoteConsumer(consumerPropertiesFileName, localTopicName);
    }

    public void addQuoteInQueue(Quote quote) {
        producerInnerReplica.addQuoteInQueue(quote);
        producerOuterReplica.addQuoteInQueue(quote);
    }

    public List<Quote> readQuotesFromQueue() {
        return consumer.readQuotesFromQueue();
    }

    public void saveQuotes(List<Quote> quotes) {
        //nothing to do
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
