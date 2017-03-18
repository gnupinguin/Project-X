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
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import ru.dins.kafka.consumer.OuterReplicaListener;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.model.quote.QuoteDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Creating interaction between {@link OuterReplicaListener} object and replica topic consumer(outer group consumers).
 * It enable auto start for outer group consumers
 *
 * @author Ilja Pavlov
 */
@Configuration @EnableKafka @Data @NoArgsConstructor
public class KafkaOuterConsumerConfig {
    /**
     * Object with properties for consumer configuration.
     * @see OuterConsumerConfig
     */
    @Autowired
    private OuterConsumerConfig configuration;

    /**
     *
     * @param topic Replica topic name for kafka-server.
     * @return Bean of {@link KafkaMessageListenerContainer} with one {@link OuterReplicaListener} object as listener.
     */
    @Bean
    public KafkaMessageListenerContainer<String, Quote> container(
            @Value("${kafka.replica-topic-name}") String topic){
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener(outerReplicaListener());
        containerProperties.setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE);
        return new KafkaMessageListenerContainer<>(outerConsumerFactory(), containerProperties);
    }

    /**
     *
     * @return {@link DefaultKafkaConsumerFactory} for replica topic consumers.
     */
    @Bean
    public ConsumerFactory<String, Quote> outerConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(outerConsumerConfigs());
    }

    /**
     *
     * @return Map with properties for replica topic consumer.
     */
    @Bean
    public Map<String, Object> outerConsumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, configuration.getSessionTimeoutMs());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, QuoteDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getGroupId());
        propsMap.put(ConsumerConfig.CLIENT_ID_CONFIG, configuration.getClientId());
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, configuration.getAutoOffsetReset());
        return propsMap;
    }

    /**
     *
     * @return Bean of {@link OuterReplicaListener}.
     */
    @Bean
    public OuterReplicaListener outerReplicaListener(){
        return new OuterReplicaListener();
    }
}
