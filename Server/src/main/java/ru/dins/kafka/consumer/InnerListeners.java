package ru.dins.kafka.consumer;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dins.kafka.producer.KafkaQuoteProducer;
import ru.dins.kafka.producer.UnsentQuoteException;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.net.ConnectException;
import java.util.SplittableRandom;

/**
 * Created by gnupinguin on 04.03.17.
 */
@Service @Data @NoArgsConstructor
public class InnerListeners  {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired @NonNull
    private KafkaQuoteProducer producer;

    @Autowired @NonNull
    private QuoteRepository repository;

    @KafkaListener(id = "localListener", topics = "${kafka.local-topic-name}", group = "inner",containerFactory = "kafkaListenerContainerFactory")
    public void listenLocalTopic(Quote quote) {
        try {
            producer.addQuote2ReplicaTopic(quote);
        } catch (UnsentQuoteException e) {
            logger.error(LoggersMessageStore.ADDING_QUOTE_TO_REPLICA_ERROR);
        }
        try {
            repository.addQuote(quote);
        } catch (ConnectException e) {
            logger.warn(LoggersMessageStore.CONNECTION_REPOSITORY_WARNING);
            try {
                producer.addQuote2ReserveTopic(quote);
            } catch (UnsentQuoteException ex) {
                logger.error(String.format(LoggersMessageStore.LOST_QUOTE_ERROR_PATTERN, quote, "listenLocalTopic"));
            }
        }
    }


    @KafkaListener(id = "reserveListener", topics = "${kafka.reserve-topic-name}", group = "inner", containerFactory = "kafkaListenerContainerFactory")
    public void listenReserveTopic(ConsumerRecord<String, Quote> record) {
        Quote quote = record.value();
        try{
            repository.addQuote(quote);
        } catch (ConnectException e){
            logger.warn("Connection to repository failed");
            try{
                producer.addQuote2ReserveTopic(quote);
            } catch (UnsentQuoteException ex){
                logger.error(String.format(LoggersMessageStore.LOST_QUOTE_ERROR_PATTERN, quote, "listenReserveTopic"));
            }
        }

    }

}
