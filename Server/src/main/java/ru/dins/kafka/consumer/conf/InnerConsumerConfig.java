package ru.dins.kafka.consumer.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Class with properties for local topic and reserve topic consumer.
 * There are in application.yml in kafka.consumer-conf section.
 */
@Data @NoArgsConstructor @Configuration
public class InnerConsumerConfig {

    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig#AUTO_COMMIT_INTERVAL_MS_DOC
     */
    @NonNull
    @Value("${kafka.consumer-conf.auto.commit.interval.ms}")
    private String autoCommitIntervalMs;

    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig#SESSION_TIMEOUT_MS_DOC
     */
    @NonNull
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
    @Value("${kafka.consumer-conf.inner.bootstrap.servers}")
    private String bootstrapServers;

    /**
     * Id for kafka group
     */
    @NonNull
    @Value("${kafka.consumer-conf.inner.group.id}")
    private String groupId;

}
