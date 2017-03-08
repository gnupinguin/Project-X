package ru.dins.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dins.kafka.producer.conf.ProducerConfiguration;
import ru.dins.web.model.quote.Quote;

/**
 * Created by gnupinguin on 04.03.17.
 */
@Service
public class KafkaQuoteProducer implements QuoteProducer {
    @Autowired
    private KafkaTemplate<String, Quote> producer;

    @Autowired
    private ProducerConfiguration configuration;

    private void addQuote2Topic(Quote quote, String topic) throws UnsentQuoteException {
        try{
            producer.send(topic, quote).get();
        } catch (Exception e){
            throw new UnsentQuoteException(String.format("Error adding %s to %s topic.", quote.toString(), topic), e);
        }
    }

    @Override
    public void addQuote2LocalTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, configuration.getLocalTopicName());
    }

    @Override
    public void addQuote2ReserveTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, configuration.getReserveTopicName());
    }

    @Override
    public void addQuote2ReplicaTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, configuration.getReplicaTopicName());
    }
}
