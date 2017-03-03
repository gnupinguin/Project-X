package ru.dins.kafka.synchronization;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dins.kafka.consumer.QuoteConsumer;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.util.List;

/**
 * Created by gnupinguin on 03.03.17.
 */
@Data
public class AbstractQuoteSynchronizer {
    @Autowired
    protected QuoteRepository repository;

    @Autowired
    protected QuoteProducer producer;


    /**
     * Resends quotes
     */
    protected void resendQuotes(QuoteConsumer consumer) {
        if (repository.availableConnection()){
            List<Quote> nonReceivedQuotes = consumer.readQuotesFromQueue();
            if (nonReceivedQuotes != null){
                for (Quote quote : nonReceivedQuotes) {
                    if (repository.availableConnection()){
                        repository.addQuote(quote);
                    }else{
                        producer.addQuote2ReservePartitionLocalTopic(quote);
                    }
                }
            }
        }
    }
}
