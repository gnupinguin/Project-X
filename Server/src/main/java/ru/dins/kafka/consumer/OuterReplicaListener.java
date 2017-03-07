package ru.dins.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.net.ConnectException;
import java.util.Map;

/**
 * Created by gnupinguin on 06.03.17.
 */
public class OuterReplicaListener implements AcknowledgingMessageListener<String, Quote>, ConsumerSeekAware {
    private ConsumerSeekCallback consumerSeekCallback;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private QuoteRepository repository;

    @Override
    public void onMessage(ConsumerRecord<String, Quote> data, Acknowledgment acknowledgment) {
        Quote quote = data.value();
        try{
            repository.addQuote(quote);
            acknowledgment.acknowledge();
        } catch (ConnectException e){
            logger.warn("Connection to repository failed");
            try {
                consumerSeekCallback.seek(data.topic(), data.partition(), data.offset());
            } catch (Exception ex){
                logger.error(quote + " was lost in OuterReplicaListener");
            }
        }
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
        this.consumerSeekCallback = callback;
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        //nothing to do
    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekAware.ConsumerSeekCallback callback) {
        //nothing to do
    }
}
