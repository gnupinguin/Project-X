package ru.dins.kafka.consumer.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import ru.dins.kafka.consumer.OuterReplicaListener;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.model.quote.QuoteDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gnupinguin on 06.03.17.
 */
@Configuration @EnableKafka @Data @NoArgsConstructor
public class KafkaOuterConsumerConfig {

    @Autowired
    private OuterConsumerConfig configuration;

    @Bean
    public KafkaMessageListenerContainer<String, Quote> container(
            @Value("${kafka.replica-topic-name}") String topic){
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener(outerReplicaListener());
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        return new KafkaMessageListenerContainer<>(outerConsumerFactory(), containerProperties);
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Quote>> outerKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Quote> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(outerConsumerFactory());
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Quote> outerConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(outerConsumerConfigs());
    }


    @Bean
    public Map<String, Object> outerConsumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, configuration.getEnableAutoCommit());
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, configuration.getAutoCommitIntervalMs());
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, configuration.getSessionTimeoutMs());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, QuoteDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getGroupId());
        propsMap.put(ConsumerConfig.CLIENT_ID_CONFIG, configuration.getClientId());
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, configuration.getAutoOffsetReset());
        return propsMap;
    }

    @Bean
    public OuterReplicaListener outerReplicaListener(){
        return new OuterReplicaListener();
    }
}
