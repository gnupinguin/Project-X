package ru.dins.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import ru.dins.web.model.quote.Quote;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by gnupinguin on 26.02.17.
 */
@Configuration
public class KafkaConfig {
    @Bean
    public KafkaProducer<String, Quote> kafkaProducer(@Value("${kafka.producer-path}") String filename){
        try{
            Properties props = new Properties();
            props.load(new ClassPathResource(filename).getInputStream());
            return new KafkaProducer<>(props);
        } catch (IOException e){
            System.err.println("Error with creating producer");
            System.err.println(e);
        }
        return null;
    }

}
