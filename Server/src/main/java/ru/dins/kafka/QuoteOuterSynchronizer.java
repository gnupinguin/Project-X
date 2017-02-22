package ru.dins.kafka;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.kafka.common.errors.WakeupException;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.model.quote.Quote;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */
public class QuoteOuterSynchronizer implements Runnable{
    private DBCollection quotesDbCollection;
    private ProjectXConsumer consumer;
    private AtomicBoolean closed = new AtomicBoolean(false);

    public QuoteOuterSynchronizer(ProjectXConsumer consumer, String host, int port, String dbName, String collectionName){
        MongoClient mongoClient = new MongoClient(host, port);
        DB db = mongoClient.getDB(dbName);
        quotesDbCollection = db.getCollection(collectionName);
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            while (!closed.get()){
                List<Quote> quotes = consumer.readQuotesFromQueue();
                for (Quote quote : quotes) {
                    quotesDbCollection.insert(new BasicDBObject("quoteText", quote.getQuoteText())
                            .append("quoteAuthor", quote.getQuoteAuthor())
                            .append("_class", quote.getClass().getCanonicalName()));
                }
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        }finally {
            System.out.println("OUTER SYNC INTERRUPTED");
        }
    }
}
