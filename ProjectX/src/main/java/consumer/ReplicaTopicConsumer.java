package consumer;

import java.util.Properties;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;
import quote.Quote;

import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;


public class ReplicaTopicConsumer {
    public static void main(String[] args) throws Exception {
       // if(args.length < 2){
       //     System.out.println("Usage: consumer <topic> <groupname>");
       //     return;
        // }

        String topic = "quote-replica";
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.62.221:9092");
        props.put("group.id", "0");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "quote.QuoteDeserializer");
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


