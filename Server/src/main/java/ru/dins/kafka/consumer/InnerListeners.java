package ru.dins.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import ru.dins.Application;
import ru.dins.kafka.producer.KafkaQuoteProducer;
import ru.dins.kafka.producer.UnsentQuoteException;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.util.concurrent.CountDownLatch;

/**
 * Created by gnupinguin on 04.03.17.
 */
@Service
public class InnerListeners {

    @Autowired
    private KafkaQuoteProducer producer;

    @Autowired
    private QuoteRepository repository;

    @Autowired
    ApplicationContext context;



    @KafkaListener(id = "localListener", topics = "${kafka.local-topic-name}", group = "inner")
    public void listenLocalTopic(Quote quote) {
        if (repository.availableConnection()){
            repository.addQuote(quote);
            try{
                producer.addQuote2ReplicaTopic(quote);
            } catch (UnsentQuoteException e){
                try{
                    producer.addQuote2ReserveTopic(quote);
                } catch (UnsentQuoteException ex){
                    System.err.println(quote + "lost!!!!");
                }
            }
        }else{
            System.err.println("Error adding quote");
        }
    }

    @KafkaListener(id = "reserveListener", topics = "${kafka.reserve-topic-name}", group = "inner")
    public void listenReserveTopic(ConsumerRecord<String, Quote> record) {
        Quote quote = record.value();
        if (repository.availableConnection()){
            repository.addQuote(quote);
//            try{
//                producer.addQuote2ReserveTopic(quote);
//            } catch (UnsentQuoteException e){
//                System.err.println(quote + "lost!!!!");
//            }
        }else{
            System.err.println("Error adding quote");
        }
    }

}
