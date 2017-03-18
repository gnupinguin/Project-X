package ru.dins.kafka.producer.conf;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.model.quote.QuoteSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Creating interaction between {@link KafkaTemplate} and producer for topics on local kafka-server.
 * It enable auto start for producer.
 *
 * @author Pavlov Ilja
 */
@Configuration @Data
@NoArgsConstructor @EnableKafka
public class KafkaProducerConfig {
    /**
     * @see ProducerConfiguration
     */
    @Autowired
    private ProducerConfiguration configuration;

    /**
     *
     * @return {@link DefaultKafkaProducerFactory} for creating producer.
     */
    @Bean
    public ProducerFactory<String, Quote> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     *
     * @return Map with properties for producer.
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
        props.put(ProducerConfig.RETRIES_CONFIG, configuration.getRetries());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, configuration.getBatchSize());
        props.put(ProducerConfig.LINGER_MS_CONFIG, configuration.getLingerMs());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, configuration.getBufferMemory());
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, configuration.getMaxBlockMs());//for throw connection exception
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, QuoteSerializer.class);
        return props;
    }

    /**
     *
     * @return Bean of {@link KafkaTemplate}.
     */
    @Bean
    public KafkaTemplate<String, Quote> kafkaTemplate() {
        return new KafkaTemplate<String, Quote>(producerFactory());
    }
}

