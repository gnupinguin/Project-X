package ru.dins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import ru.dins.kafka.LocalKafkaQuoteSynchonizer;
import ru.dins.kafka.QuoteSynchronizer;
import ru.dins.kafka.consumer.LocalTopicConsumerThread;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.kafka.consumer.QuoteConsumer;
import ru.dins.kafka.consumer.ReplicaTopicConsumerThread;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.model.quote.Quote;

import java.util.List;

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

        QuoteProducer producer = new QuoteProducer("src/main/kafka-conf/producer.properties", localTopicName);
        QuoteProducer innerReplicaProducer = new QuoteProducer("src/main/kafka-conf/innerReplicaProducer.properties", replicaTopicName);

        QuoteSynchronizer localKafkaSynchonizer = new LocalKafkaQuoteSynchonizer(innerReplicaProducer, innerLocalConsumer,
                "localhost", 27017, "QuotesDB", "quotes");

        QuoteSynchronizer adjacentKafkaSynchronizer = new QuoteSynchronizer(producer, outerReplicaConsumer);



//        QuoteConsumer replicaConsumer = new QuoteConsumer("src/main/kafka-conf/replicaTopicConsumer.properties", replicaTopicName);
//        ReplicaTopicConsumerThread replicaTopicConsumerThread = new ReplicaTopicConsumerThread(replicaConsumer,
//                "localhost", 27017, "QuotesDB", "quotes");


        new Thread(localKafkaSynchonizer).start();
        new Thread(adjacentKafkaSynchronizer).start();


        SpringApplication.run(Application.class, args);
    }
}
