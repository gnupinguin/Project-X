package ru.dins.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import ru.dins.kafka.producer.KafkaQuoteProducer
import ru.dins.web.model.quote.Quote
import ru.dins.web.persistence.QuoteRepository
import spock.lang.Specification
import spock.lang.Subject

class InnerListenersTest extends Specification {
    public static final String TOPIC = 'topic'
    public static final int PARTITION = 1

    @Subject InnerListeners listeners

    KafkaQuoteProducer producer = Mock()
    QuoteRepository repository = Mock()
    Quote quote = Mock()
    ConsumerRecord<String, Quote> consumerRecord = new ConsumerRecord<>(TOPIC, PARTITION, 0, null, quote)

    def setup(){
        listeners = new InnerListeners()
        listeners.producer = producer
        listeners.repository = repository
    }

    def "when listener to local-topic available then not add to repository"() {
        given:
        repository.addQuote(quote) >> {throw new ConnectException()}

        when:
        listeners.listenLocalTopic(quote)

        then:
        1 * producer.addQuote2ReserveTopic(quote)
    }

    def "if ConnectException then add to reserve topic"() {
        given:
        repository.addQuote(quote) >> {throw new ConnectException()}

        when:
        listeners.listenReserveTopic(quote)

        then:
        1 * producer.addQuote2ReserveTopic(quote)
    }

}