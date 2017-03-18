package ru.dins.kafka.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dins.LoggersMessageStore;
import ru.dins.kafka.producer.KafkaQuoteProducer;
import ru.dins.kafka.producer.UnsentQuoteException;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.net.ConnectException;

/**
 * The class contains two methods-listener of the local(inner) kafka-server.
 *
 * @author Daria Bykova
 * @author Ilja Pavlov
 */
@Service @Data @NoArgsConstructor @Slf4j
public class InnerListeners  {
    /**
     * It's a producer for sending quotes to topics on kafka-server.
     * @see KafkaQuoteProducer
     */
    @Autowired @NonNull
    private KafkaQuoteProducer producer;

    /**
     * It's a client of repository for inserting quotes to database.
     *  @see QuoteRepository
     */
    @Autowired @NonNull
    private QuoteRepository repository;

    /**
     * <p>
     *     This method reacts to event of quote-adding in a local topic.
     * It reads quote from local topic and trying to write message to database.
     * If database is not available, it trying to send quote on reserve topic of local kafka-server.
     * If local kafka-server is not available, quote will be lost.
     *
     * <p>
     *     For annotation  {@code @KafkaListener} using some parameters:
     * <ul>
     *     <li>
     *         For kafka this method marked as {@code id = "localListener"}.
     *     </li>
     *     <li>
     *         For detecting factory-bean of spring kafka using {@code containerFactory = "kafkaListenerContainerFactory"}
     *     </li>
     * </ul>
     * Parameters {@code group} and {@code topics} are in application.yml
     * @param quote the quote for writing to database.
     */
    @KafkaListener(id = "localListener", topics = "${kafka.local-topic-name}",
            group = "${kafka.consumer-conf.inner.group.id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenLocalTopic(Quote quote) {
        try {
            producer.addQuote2ReplicaTopic(quote);
        } catch (UnsentQuoteException e) {
            log.error(LoggersMessageStore.ADDING_QUOTE_TO_REPLICA_ERROR);
        }
        try {
            repository.addQuote(quote);
        } catch (ConnectException e) {
            log.warn(LoggersMessageStore.CONNECTION_REPOSITORY_WARNING);
            try {
                producer.addQuote2ReserveTopic(quote);
            } catch (UnsentQuoteException ex) {
                log.error(String.format(LoggersMessageStore.LOST_QUOTE_ERROR_PATTERN, quote, "listenLocalTopic"));
            }
        }
    }

    /**
     * <p>
     *     This method reacts to event of quote-adding in a reserve topic.
     * It reads quote from reserve topic and trying to write message to database.
     * If database is not available, it trying to send quote on reserve topic local kafka-server.
     * If local kafka-server is not available, quote will be lost.
     * <p>
     *     For annotation  @KafkaListener using some parameters:<ul>
     *         <li>
     *         For kafka this method marked as {@code id = "reserveListener"}.
     *     </li>
     *     <li>
     *         For detecting factory-bean of spring kafka using {@code containerFactory = "kafkaListenerContainerFactory"}
     *     </li>
     * </ul>
     * Parameters {@code group} and {@code topics} are in application.yml
     * @param quote the quote for writing to database.
     */
    @KafkaListener(id = "reserveListener", topics = "${kafka.reserve-topic-name}",
            group = "${kafka.consumer-conf.inner.group.id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenReserveTopic(Quote quote) {

        try{
            repository.addQuote(quote);
        } catch (ConnectException e){
            log.warn("Connection to repository failed");
            try{
                producer.addQuote2ReserveTopic(quote);
            } catch (UnsentQuoteException ex){
                log.error(String.format(LoggersMessageStore.LOST_QUOTE_ERROR_PATTERN, quote, "listenReserveTopic"));
            }
        }

    }

}
