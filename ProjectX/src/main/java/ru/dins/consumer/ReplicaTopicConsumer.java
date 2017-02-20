package ru.dins.consumer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.mongodb.BasicDBObject;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import ru.dins.model.quote.Quote;


public class ReplicaTopicConsumer extends QuoteConsumer  {
    DBCollection quotesDbCollection;

    public ReplicaTopicConsumer(Properties props, String topicName, String host, int port, String dbName, String collectionName) {
        super(props, topicName);
        MongoClient mongoClient = new MongoClient( host, port );
        DB db = mongoClient.getDB( dbName);
        quotesDbCollection = db.getCollection(collectionName  );

    }

    public ReplicaTopicConsumer(String consunerPropertiesFilename, String topicName,
             String host, int port, String dbName, String collectionName) throws IOException {
        super(consunerPropertiesFilename, topicName); MongoClient mongoClient = new MongoClient( host, port );
        DB db = mongoClient.getDB( dbName);
        quotesDbCollection = db.getCollection(collectionName  );

    }

    @Override
    public List<Quote> readQuotesFromQueue(){
        return readQuotesFromQueue();
    }

    @Override
    public void saveQuotes(List<Quote> quotes) {
        for (Quote quote : quotes){
            quotesDbCollection.insert(new BasicDBObject("quoteText", quote.getQuoteText())
                    .append("quoteAuthor", quote.getQuoteAuthor())
                    .append("_class", quote.getClass().getCanonicalName()));
        }
    }

    @Override
    public void close() {
        super.close();
    }
//    public static void main(String[] args) throws Exception {
//
//
//        String topic = "ru.dins.quote-replica";
//        Properties props = new Properties();
//        props.load(new FileInputStream("target/config/local-ru.dins.consumer/PropConsumer.properties"));
//
//        KafkaConsumer<String, Quote> consumer = new KafkaConsumer<String, Quote>(props);
//
//        consumer.subscribe(Arrays.asList(topic));
//        int i = 0;
//
//        MongoClient mongoClient = new MongoClient( "localhost", 27017 );
//        DB db = mongoClient.getDB( "QuotesDB" );
//        DBCollection coll = db.getCollection("quotes");
//
//
//       while (true) {
//           ConsumerRecords<String, Quote> records = consumer.poll(100);
//           for (ConsumerRecord<String, Quote> record : records) {
//               BasicDBObject Quote = new BasicDBObject("author",
//                       record.value().getQuoteAuthor()).append("quotestext", record.value().getQuoteText());
//               coll.insert(Quote);
//
//           }
//        }
//    }


}


