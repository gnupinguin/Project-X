package ru.dins.kafka.producer;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.dins.web.model.quote.Quote;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by dins on 16.02.17.
 */

@Service @Data @NoArgsConstructor
public class QuoteProducerImpl implements QuoteProducer {
    private AtomicLong key = new AtomicLong(0);
    @Autowired
    private Producer<String, Quote> producer;

    @Value("${kafka.local-topic-name}")
    private String localTopicName;

    @Value("${kafka.replica-topic-name}")
    private  String replicaTopicName;

    @Value("${kafka.main-partition-local-topic}")
    private int mainPartitionOfLocalTopic;

    @Value("${kafka.reserve-partition-local-topic}")
    private int reservePartitionOfLocalTopic;

//    @Override
//    public void addQuote2LocalTopic(Quote quote) {
//        producer.send(new ProducerRecord<>(getLocalTopicName(), Long.toString(key.getAndIncrement()), quote));
//    }

    @Override
    public void addQuote2ReplicaTopic(Quote quote) {
        producer.send(new ProducerRecord<>(getReplicaTopicName(), Long.toString(key.getAndIncrement()), quote));
    }

    @Override
    public void addQuote2MainPartitionLocalTopic(Quote quote) {
        producer.send(new ProducerRecord<>(getLocalTopicName(), mainPartitionOfLocalTopic, Long.toString(key.getAndIncrement()), quote));
    }

    @Override
    public void addQuote2ReservePartitionLocalTopic(Quote quote) {
        producer.send(new ProducerRecord<>(getLocalTopicName(), reservePartitionOfLocalTopic, Long.toString(key.getAndIncrement()), quote));
    }

    @Override
    public void close(){
        producer.close();
    }
}
