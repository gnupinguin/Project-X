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
 * A simple class for controlling access to the MongoDB.
 * Property of collection name is application.yml.
 * As bean for {@link MongoTemplate} using {@link MongoConfig#anotherMongoTemplate()} bean.
 */
@Repository @NoArgsConstructor
public class QuoteRepository {

    @Value("${spring.data.mongodb.collection}")
    private String quotesCollection;

    @Autowired
    @Qualifier("anotherMongoTemplate")
    private MongoTemplate mongoTemplate;

    /**
     *
     * @return List of all quotes from database.
     * @throws ConnectException On connection to MongoDB failed.
     */
    public List<Quote> findAll() throws ConnectException {
        try {
            return mongoTemplate.findAll(Quote.class, quotesCollection);
        } catch (Exception e){
            throw new ConnectException("Error connect to DB when finding  quotes");
        }
    }

    /**
     * Adding quote to database.
     * @param quote Quote for adding to database.
     * @throws ConnectException On connection to MongoDB failed.
     */
    public void addQuote(Quote quote) throws ConnectException {
        try{
            mongoTemplate.insert(quote, quotesCollection);
        } catch (Exception e){
            throw new ConnectException("Error connect to DB when adding " + quote);
        }
    }
}
