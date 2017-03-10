package ru.dins.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dins.kafka.producer.conf.ProducerConfiguration;
import ru.dins.web.model.quote.Quote;

/**
 * Implementation {@code QuoteProducer}.
 * @see QuoteProducer
 */
@Service
public class KafkaQuoteProducer implements QuoteProducer {
    /**
     * Kafka producer for sending quotes.
     */
    @Autowired
    private KafkaTemplate<String, Quote> producer;

    /**
     * Object with some properties for kafka-producer
     */
    @Autowired
    private ProducerConfiguration configuration;

    /**
     * Sending the quote to kafka topic.
     * @param quote The quote for sending
     * @param topic Topic name of topic on kafka-server
     * @throws UnsentQuoteException On error with sending quote.
     */
    private void addQuote2Topic(Quote quote, String topic) throws UnsentQuoteException {
        try{
            producer.send(topic, quote).get();
        } catch (Exception e){
            throw new UnsentQuoteException(String.format("Error adding %s to %s topic.", quote.toString(), topic), e);
        }
    }

    /**
     *
     * @param quote Quote for sending.
     * @throws UnsentQuoteException
     * @see QuoteProducer#addQuote2LocalTopic(Quote)
     */
    @Override
    public void addQuote2LocalTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, configuration.getLocalTopicName());
    }

    /**
     *
     * @param quote Quote for sending.
     * @throws UnsentQuoteException
     * @see QuoteProducer#addQuote2ReserveTopic(Quote)
     */
    @Override
    public void addQuote2ReserveTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, configuration.getReserveTopicName());
    }

    /**
     *
     * @param quote Quote for sending.
     * @throws UnsentQuoteException
     * @see QuoteProducer#addQuote2ReplicaTopic(Quote)
     */
    @Override
    public void addQuote2ReplicaTopic(Quote quote) throws UnsentQuoteException {
        addQuote2Topic(quote, configuration.getReplicaTopicName());
    }
}
