package ru.dins.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import ru.dins.web.model.quote.Quote
import spock.lang.Specification
import spock.lang.Subject

class KafkaQuoteProducerTest extends Specification {
    public static final String TOPIC = 'topic'

    @Subject KafkaQuoteProducer kafkaQuoteProducer

    KafkaTemplate<String, Quote> producer = Mock()
    Quote quote = Mock()

    def setup() {
        kafkaQuoteProducer = new KafkaQuoteProducer()
        kafkaQuoteProducer.producer
    }

    def "test1"() {
        given:
        producer.send(TOPIC, quote).get() >> {throw new Exception()}

        when:
        kafkaQuoteProducer.addQuote2Topic(quote, TOPIC)

        then:
        throw new Exception()

    }

}
