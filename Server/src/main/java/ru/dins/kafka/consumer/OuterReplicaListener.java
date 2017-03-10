package ru.dins.kafka.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import ru.dins.LoggersMessageStore;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.net.ConnectException;
import java.util.Map;

/**
 * Created by gnupinguin on 06.03.17.
 *
 * The class is specific kafka-listener.
 * For manipulating offsets, the class implementing {@code AcknowledgingMessageListener, ConsumerSeekAware}.
 * @see AcknowledgingMessageListener
 * @see ConsumerSeekAware
 */
@Data @NoArgsConstructor @Slf4j
public class OuterReplicaListener implements AcknowledgingMessageListener<String, Quote>, ConsumerSeekAware {
    private ConsumerSeekCallback consumerSeekCallback;
    @Autowired @NonNull
    private QuoteRepository repository;

    /**
     * This method reacts to event of quote-adding in a replica topic other(outer) kafka-server.
     * It reads quote from replica topic outer kafka-server and trying to write message to database.
     * If database not available, it don't commit offset of quote.
     * @param data {@link ConsumerRecord metadata of quote} from kafka-server.
     * @param acknowledgment {@link Acknowledgment callback} for commiting offset
     */
    @Override
    public void onMessage(ConsumerRecord<String, Quote> data, Acknowledgment acknowledgment) {
        Quote quote = data.value();
        try{
            repository.addQuote(quote);
            acknowledgment.acknowledge();
        } catch (ConnectException e){
            log.warn(LoggersMessageStore.CONNECTION_REPOSITORY_WARNING);
            try {
                consumerSeekCallback.seek(data.topic(), data.partition(), data.offset());
            } catch (Exception ex){
                log.error(String.format(LoggersMessageStore.LOST_QUOTE_ERROR_PATTERN, quote, "onMessage"));
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
