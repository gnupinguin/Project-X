package ru.dins.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import ru.dins.model.quote.Quote;
import ru.dins.kafka.producer.ProjectXProducer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by gnupinguin on 16.02.17.
 */
@AllArgsConstructor
public class LocalTopicConsumerThread implements Runnable{
    @NonNull
    private ProjectXProducer producerInnerReplica;

    @NonNull
    private ProjectXProducer producerOuterReplica;

    @NonNull
    private ProjectXConsumer consumer;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public void run(){
        try{
            while (!closed.get()){
                List<Quote> quotes = consumer.readQuotesFromQueue();
                for (Quote quote : quotes){
                    producerInnerReplica.addQuoteInQueue(quote);
                    producerOuterReplica.addQuoteInQueue(quote);

                }
            }
        }catch (WakeupException e){
            if (!closed.get()) throw e;
        } finally {
            System.out.println("\nLocalTopicConsumerThread was interrupted\n");
            producerInnerReplica.close();
            producerOuterReplica.close();
            consumer.close();
        }
    }

}
