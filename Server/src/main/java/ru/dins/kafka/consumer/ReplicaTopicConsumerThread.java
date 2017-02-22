package ru.dins.kafka.consumer;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.apache.kafka.common.errors.WakeupException;

import ru.dins.model.quote.Quote;

import java.util.concurrent.atomic.AtomicBoolean;

public class ReplicaTopicConsumerThread implements Runnable  {
    private DBCollection quotesDbCollection;
    private ProjectXConsumer consumer;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ReplicaTopicConsumerThread(ProjectXConsumer consumer, String host, int port, String dbName, String collectionName) {
        this.consumer = consumer;
        MongoClient mongoClient = new MongoClient(host, port);
        DB db = mongoClient.getDB(dbName);
        quotesDbCollection = db.getCollection(collectionName);
    }

    @Override
    public void run() {
        try {
            while (!closed.get()) {
                for (Quote quote : consumer.readQuotesFromQueue()) {
                    quotesDbCollection.insert(new BasicDBObject("quoteText", quote.getQuoteText())
                            .append("quoteAuthor", quote.getQuoteAuthor())
                            .append("_class", quote.getClass().getCanonicalName()));
                }
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        } finally {
            System.out.println("\nReplicaTopicConsumer was interrupted\n");
            consumer.close();
        }
    }
}


