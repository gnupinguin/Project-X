package ru.dins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import ru.dins.kafka.QuoteLocalSynchronizer;
import ru.dins.kafka.QuoteOuterSynchronizer;
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

//        QuoteProducer innerLocalProducer = new QuoteProducer("src/main/kafka-conf/producer.properties", localTopicName);
//
        QuoteProducer innerReplicaProducer = new QuoteProducer("src/main/kafka-conf/innerReplicaProducer.properties", replicaTopicName);

        QuoteLocalSynchronizer quoteLocalSynchronizer = new QuoteLocalSynchronizer(innerReplicaProducer, innerLocalConsumer,
                "localhost", 27017, "QuotesDB", "quotes");

        QuoteOuterSynchronizer quoteOuterSynchronizer = new QuoteOuterSynchronizer(outerReplicaConsumer,
                "localhost", 27017, "QuotesDB", "quotes");


        new Thread(quoteOuterSynchronizer).start();
        new Thread(quoteLocalSynchronizer).start();


        SpringApplication.run(Application.class, args);
    }
}
