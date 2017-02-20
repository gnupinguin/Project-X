package ru.dins.consumer;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import ru.dins.model.quote.Quote;


public class ReplicaTopicConsumer {
    public static void main(String[] args) throws Exception {


        String topic = "ru.dins.quote-replica";
        Properties props = new Properties();
        props.load(new FileInputStream("target/config/local-ru.dins.consumer/PropConsumer.properties"));

        KafkaConsumer<String, Quote> consumer = new KafkaConsumer<String, Quote>(props);

        consumer.subscribe(Arrays.asList(topic));
        int i = 0;

        MongoClient mongoClient = new MongoClient( "localhost", 27017 );
        DB db = mongoClient.getDB( "QuotesDB" );
        DBCollection coll = db.getCollection("quotes");


       while (true) {
           ConsumerRecords<String, Quote> records = consumer.poll(100);
           for (ConsumerRecord<String, Quote> record : records) {
               BasicDBObject Quote = new BasicDBObject("author",
                       record.value().getQuoteAuthor()).append("quotestext", record.value().getQuoteText());
               coll.insert(Quote);

           }
        }
    }
}


