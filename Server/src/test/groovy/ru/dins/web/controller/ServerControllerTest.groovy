package ru.dins.web.controller

import org.springframework.ui.Model
import ru.dins.kafka.producer.QuoteProducer
import ru.dins.kafka.producer.UnsentQuoteException
import ru.dins.web.model.quote.Quote
import ru.dins.web.persistence.QuoteRepository
import spock.lang.Specification
import spock.lang.Subject

class ServerControllerTest extends Specification {
    public static final String QUOTETEXT = 'quote'
    public static final String AUTOR = 'autor'
    public static final String STATUS = 'status'
    public static final String REMOTEHOST = 'remoteHost'
    public static final String AUT = ''
    public static final String STAT = ''


    @Subject ServerController serverController

    QuoteRepository repository = Mock()
    QuoteProducer producer = Mock()
    Quote quote = Mock()
    UnsentQuoteException exception = new UnsentQuoteException()

    def setup() {
        serverController = new ServerController()
        serverController.repository = repository
        serverController.producer = producer
    }

    def "addQuote return 'status'"() {
        when:
        serverController.addQuote(QUOTETEXT, AUTOR, Mock(Model))

        then:
        STATUS
    }

    def "for exception"() {
        given:
        producer.addQuote2LocalTopic(quote) >> {throw new UnsentQuoteException()}

        when:
        serverController.addQuote(QUOTETEXT, AUTOR, Mock(Model))

        then:
        String.format("redirect:%s/add?quote=%s&author=%s",REMOTEHOST,QUOTETEXT,AUTOR)
    }

    def "for if"() {
        when:
        serverController.addQuote(STAT, AUT, Mock(Model))

        then:
        STATUS
    }

    def "for retutn in showQuotes"() {
        when:
        serverController.showQuotes(Mock(Model))

        then:
        'data'
    }

    def "for exception in showQuotes"() {
        given:
        repository.findAll() >> {throw new ConnectException()}

        when:
        serverController.showQuotes(Mock(Model))

        then:
        "redirect:" + REMOTEHOST + "/data"
    }

    def "for index"() {
        when:
        serverController.index()

        then:
        'index.html'
    }

    def "createQuote"() {
        when:
        serverController.createQuote()

        then:
        'create-quote.html'
    }
}
