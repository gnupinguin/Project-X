package ru.dins.kafka.synchronization;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import lombok.Data;
import lombok.NonNull;
import ru.dins.model.quote.Quote;

import java.util.List;

/**
 * Created by gnupinguin on 23.02.17.
 */
@Data
public abstract class Synchronizer implements Runnable {
    @NonNull
    private DBCollection quotesDbCollection;
    public void perform(List<Quote> quotes){
        for (Quote quote : quotes) {
            quotesDbCollection.insert(new BasicDBObject("quoteText", quote.getQuoteText())
                    .append("quoteAuthor", quote.getQuoteAuthor())
                    .append("_class", quote.getClass().getCanonicalName()));
        }
    }
}
