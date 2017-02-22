package ru.dins.kafka;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.WakeupException;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.model.quote.Quote;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */

public class QuoteLocalSynchronizer implements Runnable {
    @NonNull
    private ProjectXProducer producer;
    @NonNull
    private ProjectXConsumer consumer;

    private DBCollection quotesDbCollection;

    private AtomicBoolean closed = new AtomicBoolean(false);

    public QuoteLocalSynchronizer(ProjectXProducer producer, ProjectXConsumer consumer,
                                  String host, int port, String dbName, String collectionName){
        MongoClient mongoClient = new MongoClient(host, port);
        DB db = mongoClient.getDB(dbName);
        quotesDbCollection = db.getCollection(collectionName);
        this.producer = producer;
        this.consumer = consumer;
    }



    @Override
    public void run() {
        try{
            while (!closed.get()){
                List<Quote> quotes = consumer.readQuotesFromQueue();
                perform(quotes);
                for (Quote quote : quotes){
                    producer.addQuoteInQueue(quote);
                    System.out.println(quote);
                }
            }
        } catch (WakeupException e){
            if (!closed.get()){
                throw e;
            }
        } finally {
            System.out.println("\nLOCAL SYNC IS INTERRUPTED\n");
            consumer.close();
            producer.close();
        }
    }

    protected void perform(List<Quote> quotes){
        //nothing to do
        try {
            for (Quote quote : quotes) {
                quotesDbCollection.insert(new BasicDBObject("quoteText", quote.getQuoteText())
                        .append("quoteAuthor", quote.getQuoteAuthor())
                        .append("_class", quote.getClass().getCanonicalName()));
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        }
    }


}
