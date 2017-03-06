package ru.dins.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.util.Map;

/**
 * Created by gnupinguin on 06.03.17.
 */
public class OuterReplicaListener implements AcknowledgingMessageListener<String, Quote>, ConsumerSeekAware {
    private ConsumerSeekCallback consumerSeekCallback;
    @Autowired
    private QuoteRepository repository;

    @Override
    public void onMessage(ConsumerRecord<String, Quote> data, Acknowledgment acknowledgment) {
        Quote quote = data.value();
        if (repository.availableConnection()){
            repository.addQuote(quote);
            acknowledgment.acknowledge();
        }else{
            try {
                consumerSeekCallback.seek(data.topic(), data.partition(), data.offset());
            } catch (Exception e){
                System.err.println(quote + " was lost in OuterReplicaListener");
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
