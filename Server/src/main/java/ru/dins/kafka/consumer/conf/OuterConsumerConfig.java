package ru.dins.kafka.consumer.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Class with properties for replica topic consumer.
 * There are in application.yml in kafka.consumer-conf section.
 */
@Data
@NoArgsConstructor
@Configuration
public class OuterConsumerConfig {
    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig#ENABLE_AUTO_COMMIT_DOC
     */
    @Value("${kafka.consumer-conf.outer.enable.auto.commit}")
    private String enableAutoCommit;

    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig#AUTO_COMMIT_INTERVAL_MS_DOC
     */
    @Value("${kafka.consumer-conf.auto.commit.interval.ms}")
    private String autoCommitIntervalMs;

    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig#SESSION_TIMEOUT_MS_DOC
     */
    @Value("${kafka.consumer-conf.session.timeout.ms}")
    private String sessionTimeoutMs;

    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig#AUTO_OFFSET_RESET_DOC
     */
    @NonNull
    @Value("${kafka.consumer-conf.auto.offset.reset}")
    private String autoOffsetReset;

    /**
     * Addresses of kafka servers
     */
    @NonNull
    @Value("${kafka.consumer-conf.outer.bootstrap.servers}")
    private String bootstrapServers;

    /**
     * Id for kafka group
     */
    @NonNull
    @Value("${kafka.consumer-conf.outer.group.id}")
    private String groupId;

    /**
     * Id for kafka client.
     */
    @NonNull
    @Value("${kafka.consumer-conf.outer.client.id}")
    private String clientId;
}

