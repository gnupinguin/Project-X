package ru.dins;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import ru.dins.kafka.synchronization.QuoteLocalSynchronizer;
import ru.dins.kafka.synchronization.QuoteOuterSynchronizer;
import ru.dins.kafka.consumer.QuoteConsumer;
import ru.dins.kafka.producer.QuoteProducer;

/**
 * Created by gnupinguin on 18.02.17.
 */
@SpringBootApplication
@Controller
public class Application {
    public static void main(String[] args) throws Exception {
        String localTopicName = "quote-local";
        String replicaTopicName = "quote-replica";

        QuoteConsumer innerLocalConsumer = new QuoteConsumer("src/main/kafka-conf/localTopicConsumer.properties", localTopicName);
        QuoteConsumer outerReplicaConsumer = new QuoteConsumer("src/main/kafka-conf/outerReplicaConsumer.properties", replicaTopicName);

        QuoteProducer innerReplicaProducer = new QuoteProducer("src/main/kafka-conf/innerReplicaProducer.properties", replicaTopicName);

        DBCollection quotesDbCollection = new Mongo("localhost", 27017).getDB("QuotesDB").getCollection("quotes");
        QuoteLocalSynchronizer quoteLocalSynchronizer = new QuoteLocalSynchronizer(innerReplicaProducer, innerLocalConsumer, quotesDbCollection);

        QuoteOuterSynchronizer quoteOuterSynchronizer = new QuoteOuterSynchronizer(outerReplicaConsumer, quotesDbCollection);

        new Thread(quoteLocalSynchronizer).start();
        new Thread(quoteOuterSynchronizer).start();


        SpringApplication.run(Application.class, args);
    }
}
