package ru.dins.kafka.consumer.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by gnupinguin on 08.03.17.
 */
@Data @NoArgsConstructor @Configuration
public class InnerConsumerConfig {
    @NonNull
    @Value("${kafka.consumer-conf.inner.enable.auto.commit}")
    private String enableAutoCommit;

    @NonNull
    @Value("${kafka.consumer-conf.auto.commit.interval.ms}")
    private String autoCommitIntervalMs;

    @NonNull
    @Value("${kafka.consumer-conf.session.timeout.ms}")
    private String sessionTimeoutMs;

    @NonNull
    @Value("${kafka.consumer-conf.auto.offset.reset}")
    private String autoOffsetReset;

    @NonNull
    @Value("${kafka.consumer-conf.inner.bootstrap.servers}")
    private String bootstrapServers;

    @NonNull
    @Value("${kafka.consumer-conf.inner.group.id}")
    private String groupId;

//    @Bean
//    public InnerConsumerConfig innerConsumerConfig(){
//        return new InnerConsumerConfig();
//    }
}
