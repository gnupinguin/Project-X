package ru.dins.kafka.synchronization;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.dins.kafka.consumer.QuoteConsumer;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */

@Service @NoArgsConstructor
public class QuoteOuterSynchronizer extends AbstractQuoteSynchronizer implements Runnable{

    @Autowired @Qualifier("outerReplicaTopicConsumerFromFile")
    private QuoteConsumer consumer;


    private AtomicBoolean closed = new AtomicBoolean(false);

    @Override
    public void run() {
        try {
            while (!closed.get()) {
                resendQuotes(consumer);
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        }finally {
            System.out.println("OUTER SYNC INTERRUPTED");
        }
    }
}
