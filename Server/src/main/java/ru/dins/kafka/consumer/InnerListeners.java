package ru.dins.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.dins.kafka.producer.KafkaQuoteProducer;
import ru.dins.kafka.producer.UnsentQuoteException;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.net.ConnectException;

/**
 * Created by gnupinguin on 04.03.17.
 */
@Service
public class InnerListeners  {

    @Autowired
    private KafkaQuoteProducer producer;

    @Autowired
    private QuoteRepository repository;

    @KafkaListener(id = "localListener", topics = "${kafka.local-topic-name}", group = "inner",containerFactory = "kafkaListenerContainerFactory")
    public void listenLocalTopic(Quote quote) {
            try{
                repository.addQuote(quote);
                try {
                    producer.addQuote2ReplicaTopic(quote);
                } catch (UnsentQuoteException e){
                    System.err.println("Error with adding quote to replica topic.");
                }
            }catch (ConnectException e){
                try {
                    producer.addQuote2ReserveTopic(quote);
                } catch (UnsentQuoteException ex){
                    System.err.println(quote + " was lost in listenerLocalTopic");
                }
            }
    }

    @KafkaListener(id = "reserveListener", topics = "${kafka.reserve-topic-name}", group = "inner", containerFactory = "kafkaListenerContainerFactory")
    public void listenReserveTopic(ConsumerRecord<String, Quote> record) {
        Quote quote = record.value();
        try{
            repository.addQuote(quote);
        } catch (ConnectException e){
            System.err.println("Error adding quote " + quote + " to DB in listenReserveTopic");
            try{
                producer.addQuote2ReserveTopic(quote);
            } catch (UnsentQuoteException ex){
                System.err.println(quote + " was lost in listenReserveTopic");
            }
        }

    }

}
