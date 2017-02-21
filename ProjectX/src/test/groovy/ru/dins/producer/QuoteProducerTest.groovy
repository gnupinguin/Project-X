package ru.dins.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import ru.dins.model.quote.Quote
import spock.lang.Specification
import spock.lang.Subject

@SuppressWarnings("GroovyAccessibility")
class QuoteProducerTest extends Specification {
    public static final String TOPIC = 'TOPIC'

    @Subject QuoteProducer quoteProducer

    KafkaProducer<String, Quote> producer = Mock()

    def setup() {
        quoteProducer = new QuoteProducer(producer, TOPIC)
    }

    def "test parameters for send"() {
        given:
        def quote = new Quote('1', '1')

        when:
        quoteProducer.addQuoteInQueue(quote)

        then:
        1 * producer.send(new ProducerRecord<String, Quote>(TOPIC, '0', quote))
    }

    def "test for key"() {
        expect:
        quoteProducer.incrementKey() == 0
        quoteProducer.incrementKey() == 1
    }

    def "test call to close"() {
        when:
        quoteProducer.close()

        then:
        1 * producer.close()
    }
}
