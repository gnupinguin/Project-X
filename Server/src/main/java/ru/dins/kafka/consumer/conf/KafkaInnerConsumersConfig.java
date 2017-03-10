package ru.dins.kafka.consumer.conf;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import ru.dins.kafka.consumer.InnerListeners;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.model.quote.QuoteDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Creating interaction between {@link InnerListeners} and local topic, reserve topic consumers(inner group consumers).
 * It enable auto start for inner group consumers
 */
@Configuration @EnableKafka
public class KafkaInnerConsumersConfig {
    /**
     * Object with properties for consumer configuration.
     * @see InnerConsumerConfig
     */
    @Autowired
    private InnerConsumerConfig configuration;

    /**
     *
     * @return {@link KafkaListenerContainerFactory} for creating two consumer-listeners.
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Quote>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Quote> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(2);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    /**
     *
     * @return {@link DefaultKafkaConsumerFactory} for local topic and reserve topic consumers.
     */
    @Bean
    public ConsumerFactory<String, Quote> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    /**
     *
     * @return Map with properties for local topic and reserve topic consumers.
     * It using {@link InnerConsumerConfig} for properties source.
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, configuration.getEnableAutoCommit());
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, configuration.getAutoCommitIntervalMs());
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, configuration.getSessionTimeoutMs());
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, QuoteDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, configuration.getGroupId());
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, configuration.getAutoOffsetReset());
        return propsMap;
    }

    /**
     * @return Bean of InnerListeners class.
     */
    @Bean
    public InnerListeners innerListeners() {
        return new InnerListeners();
    }
}