package ru.dins.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import ru.dins.model.Quote;

import java.util.List;

/**
 * Created by gnupinguin on 19.02.17.
 */
@Repository
public class ProjectXRepository {
    private static final String QUOTES_COLLECTION = "quotes";
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Quote> findAll() {
        return mongoTemplate.findAll(Quote.class, QUOTES_COLLECTION);
    }

    public void addQuote(Quote quote){
        mongoTemplate.insert(quote, QUOTES_COLLECTION);
    }
}
