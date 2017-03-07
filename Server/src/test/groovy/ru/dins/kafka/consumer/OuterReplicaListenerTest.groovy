package ru.dins.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import ru.dins.web.model.quote.Quote
import ru.dins.web.persistence.QuoteRepository
import spock.lang.Specification
import spock.lang.Subject

/**
 * Created by dasha on 07.03.17.
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

    def "test1"() {
        given:
        repository.addQuote(quote) >> {throw new ConnectException()}

        when:
        outerReplicaListener.onMessage(consumerRecord, Mock(Acknowledgment))

        then:
        1 * consumerSeekCallback.seek(WARNING, PARTITION, 0)
    }
}
