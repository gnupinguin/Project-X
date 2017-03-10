package ru.dins.kafka.consumer.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gnupinguin on 08.03.17.
 */
@Data
@NoArgsConstructor
@Configuration
public class OuterConsumerConfig {
    @Value("${kafka.consumer-conf.outer.enable.auto.commit}")
    private String enableAutoCommit;

    @Value("${kafka.consumer-conf.auto.commit.interval.ms}")
    private String autoCommitIntervalMs;

    @Value("${kafka.consumer-conf.session.timeout.ms}")
    private String sessionTimeoutMs;

    @NonNull
    @Value("${kafka.consumer-conf.auto.offset.reset}")
    private String autoOffsetReset;

    @NonNull
    @Value("${kafka.consumer-conf.outer.bootstrap.servers}")
    private String bootstrapServers;

    @NonNull
    @Value("${kafka.consumer-conf.outer.group.id}")
    private String groupId;

    @NonNull
    @Value("${kafka.consumer-conf.outer.client.id}")
    private String clientId;
}

