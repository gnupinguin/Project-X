package ru.dins.kafka;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.kafka.consumer.QuoteConsumer;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.kafka.synchronization.QuoteLocalSynchronizer;
import ru.dins.kafka.synchronization.QuoteOuterSynchronizer;

import java.io.IOException;

/**
 * Created by gnupinguin on 26.02.17.
 */
@Configuration
@ImportResource({"classpath*:KafkaFilesConfigurationContext.xml"})
public class ProjectXKafkaConfig {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProjectXProducer innerLocalTopicProducerFromFile(@Qualifier("innerLocalTopicProducer") String filename,
                                                            @Qualifier("localTopic")String topic){
        try{
            return new QuoteProducer(filename, topic);
        } catch (IOException e){
            System.err.println("\nError initialization inner local topic producer\n");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProjectXProducer innerReplicaTopicProducerFromFile(@Qualifier("innerReplicaTopicProducer") String filename,
                                                              @Qualifier("replicaTopic")String topic){
        try{
            return new QuoteProducer(filename, topic);
        } catch (IOException e){
            System.err.println("\nError initialization inner local topic producer\n");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProjectXConsumer innerLocalTopicConsumerFromFile(@Qualifier("innerLocalTopicConsumer") String filename,
                                                            @Qualifier("localTopic")String topic){
        try{
            return new QuoteConsumer(filename, topic);
        } catch (IOException e){
            System.err.println("\nError initialization inner local topic consumer\n");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public ProjectXConsumer outerReplicaTopicConsumerFromFile(@Qualifier("outerReplicaTopicConsumer")String filename,
                                                              @Qualifier("replicaTopic")String topic){
        try{
            return new QuoteConsumer(filename, topic);
        } catch (IOException e){
            System.err.println("\nError initialization inner local topic consumer\n");
            System.err.println(e);
        }
        return null;
    }

    @Bean
    public QuoteLocalSynchronizer quoteLocalSynchronizer(@Qualifier("innerReplicaTopicProducerFromFile")ProjectXProducer producer,
                                                         @Qualifier("innerLocalTopicConsumerFromFile")ProjectXConsumer consumer){
        return new QuoteLocalSynchronizer(producer, consumer);
    }
    @Bean
    public QuoteOuterSynchronizer quoteOuterSynchronizer(@Qualifier("innerLocalTopicConsumerFromFile")ProjectXConsumer consumer){
        return new QuoteOuterSynchronizer(consumer);
    }
}
