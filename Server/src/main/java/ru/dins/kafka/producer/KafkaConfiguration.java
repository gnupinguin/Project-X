package ru.dins.kafka.producer;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by gnupinguin on 03.03.17.
 */
@Data
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfiguration {
    private String localTopicName;
}
