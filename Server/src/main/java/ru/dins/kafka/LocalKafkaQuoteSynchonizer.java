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

/**
 * Created by gnupinguin on 22.02.17.
 */
public class LocalKafkaQuoteSynchonizer extends QuoteSynchronizer {
    private DBCollection quotesDbCollection;
    public LocalKafkaQuoteSynchonizer(ProjectXProducer producer, ProjectXConsumer consumer, String host, int port, String dbName, String collectionName) {
        super(producer, consumer);
        MongoClient mongoClient = new MongoClient(host, port);
        DB db = mongoClient.getDB(dbName);
        quotesDbCollection = db.getCollection(collectionName);
    }

    @Override
    protected void perform(List<Quote> quotes) {
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
