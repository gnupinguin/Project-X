package ru.dins.web.persistence;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import ru.dins.web.model.quote.Quote;

import java.net.ConnectException;
import java.util.List;

/**
 * Created by gnupinguin on 19.02.17.
 */
@Repository @NoArgsConstructor
public class QuoteRepository {

    @Value("${spring.data.mongodb.collection}")
    private String quotesCollection;

    @Autowired
    @Qualifier("anotherMongoTemplate")
    private MongoTemplate mongoTemplate;

    public List<Quote> findAll() throws ConnectException {
        try {
            return mongoTemplate.findAll(Quote.class, quotesCollection);
        } catch (Exception e){
            throw new ConnectException("Error connect to DB when finding  quotes");
        }
    }
    public void addQuote(Quote quote) throws ConnectException {
        try{
            mongoTemplate.insert(quote, quotesCollection);
        } catch (Exception e){
            throw new ConnectException("Error connect to DB when adding " + quote);
        }
    }
}
