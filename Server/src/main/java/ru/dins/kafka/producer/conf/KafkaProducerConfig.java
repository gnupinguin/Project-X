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

@Configuration @Data
@NoArgsConstructor @EnableKafka
public class KafkaProducerConfig {
    @Autowired
    private ProducerConfiguration configuration;

    @Bean
    public ProducerFactory<String, Quote> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

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

    @Bean
    public KafkaTemplate<String, Quote> kafkaTemplate() {
        return new KafkaTemplate<String, Quote>(producerFactory());
    }
}

