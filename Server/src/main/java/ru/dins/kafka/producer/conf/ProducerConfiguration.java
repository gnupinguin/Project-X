package ru.dins.kafka.producer.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Data
@NoArgsConstructor
@Configuration
public class ProducerConfiguration {
    @NonNull
    @Value("${kafka.producer-conf.bootstrap.servers}")
    private String bootstrapServers;

    @NonNull
    @Value("${kafka.producer-conf.retries}")
    private String retries;

    @NonNull
    @Value("${kafka.producer-conf.batch.size}")
    private String batchSize;

    @NonNull
    @Value("${kafka.producer-conf.linger.ms}")
    private String lingerMs;

    @NonNull
    @Value("${kafka.producer-conf.buffer.memory}")
    private String bufferMemory;

    @NonNull
    @Value("${kafka.producer-conf.max.block.ms}")
    private String maxBlockMs;

    @NonNull
    @Value("${kafka.local-topic-name}")
    String localTopicName;

    @NonNull
    @Value("${kafka.replica-topic-name}")
    String replicaTopicName;

    @NonNull
    @Value("${kafka.reserve-topic-name}")
    String reserveTopicName;

}
