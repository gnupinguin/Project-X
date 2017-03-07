package ru.dins.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dins.web.model.quote.Quote;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by gnupinguin on 04.03.17.
 */
@Service
public class KafkaQuoteProducer implements QuoteProducer {

    @Autowired
    KafkaTemplate<String, Quote> producer;

    @Value("${kafka.local-topic-name}")
    String localTopicName;

    @Value("${kafka.replica-topic-name}")
    String replicaTopicName;

    @Value("${kafka.reserve-topic-name}")
    String reserveTopicName;

    private void addQuote2Topic(Quote quote, String topic) throws UnsentQuoteException {
        try{
            producer.send(topic, quote).get();
        } catch (Exception e){
            throw new UnsentQuoteException(String.format("Error adding %s to %s topic.", quote.toString(), topic), e);
        }
    }

    @Override
    public void addQuote2LocalTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, localTopicName);
    }

    @Override
    public void addQuote2ReserveTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, reserveTopicName);
    }

    @Override
    public void addQuote2ReplicaTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, replicaTopicName);
    }

    @Override
    public boolean availableConnection() {
        try{
            new Socket(InetAddress.getByName("192.168.62.221"), 9092).close();
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
