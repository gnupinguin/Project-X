package ru.dins.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import ru.dins.web.model.quote.Quote
import ru.dins.web.persistence.QuoteRepository
import spock.lang.Specification
import spock.lang.Subject

/**
 * @author Daria Bykova
 */
class OuterReplicaListenerTest extends Specification {
    public static final String WARNING = 'WARNING'
    public static final int PARTITION = 234
    public static final int OFFSET = 123
    @Subject OuterReplicaListener outerReplicaListener


    ConsumerSeekAware.ConsumerSeekCallback consumerSeekCallback = Mock()
    QuoteRepository repository = Mock()
    Quote quote = Mock()
    ConsumerRecord<String, Quote> consumerRecord = new ConsumerRecord<>(WARNING, PARTITION, 0, null, quote)


    def setup() {
        outerReplicaListener = new OuterReplicaListener()
        outerReplicaListener.repository = repository
        outerReplicaListener.consumerSeekCallback = consumerSeekCallback
    }

    def "error on connection"() {
        given:
        repository.addQuote(quote) >> {throw new ConnectException()}

        when:
        outerReplicaListener.onMessage(consumerRecord, Mock(Acknowledgment))

        then:
        1 * consumerSeekCallback.seek(WARNING, PARTITION, 0)
    }
}