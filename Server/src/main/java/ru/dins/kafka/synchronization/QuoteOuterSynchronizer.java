package ru.dins.kafka.synchronization;

import com.mongodb.DBCollection;
import org.apache.kafka.common.errors.WakeupException;
import ru.dins.kafka.consumer.ProjectXConsumer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */
public class QuoteOuterSynchronizer extends Synchronizer{
    private ProjectXConsumer consumer;
    private AtomicBoolean closed = new AtomicBoolean(false);

    public QuoteOuterSynchronizer(ProjectXConsumer consumer, DBCollection quotesDbCollection){
        super(quotesDbCollection);
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            while (!closed.get()){
                perform(consumer.readQuotesFromQueue());
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        }finally {
            System.out.println("OUTER SYNC INTERRUPTED");
        }
    }
}
