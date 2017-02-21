package ru.dins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import ru.dins.kafka.consumer.LocalTopicConsumerThread;
import ru.dins.kafka.consumer.QuoteConsumer;
import ru.dins.kafka.consumer.ReplicaTopicConsumerThread;
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

        QuoteConsumer localConsumer = new QuoteConsumer("src/main/kafka-conf/localTopicConsumer.properties", localTopicName);
        QuoteProducer innerReplicaProducer = new QuoteProducer("src/main/kafka-conf/innerReplicaProducer.properties", replicaTopicName);
        QuoteProducer outerReplicaProducer = new QuoteProducer("src/main/kafka-conf/outerReplicaProducer.properties", replicaTopicName);

        LocalTopicConsumerThread localTopicConsumerThread = new LocalTopicConsumerThread(innerReplicaProducer, outerReplicaProducer, localConsumer);


        QuoteConsumer replicaConsumer = new QuoteConsumer("src/main/kafka-conf/replicaTopicConsumer.properties", replicaTopicName);
        ReplicaTopicConsumerThread replicaTopicConsumerThread = new ReplicaTopicConsumerThread(replicaConsumer,
                "localhost", 27017, "QuotesDB", "quotes");

        new Thread(localTopicConsumerThread).start();
        new Thread(replicaTopicConsumerThread).start();


        SpringApplication.run(Application.class, args);
    }
}
