package ru.dins.web.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.dins.web.model.quote.Quote;

import java.util.Collection;
import java.util.List;


/**
 * Created by gnupinguin on 19.02.17.
 */
@Repository @NoArgsConstructor
public class QuoteRepository {
    @Value("${spring.data.mongodb.collection}")
    private String quotesCollection;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Quote> findAll() {
        return mongoTemplate.findAll(Quote.class, quotesCollection);
    }
    public Quote findOne(){
        return  mongoTemplate.findOne(new Query(), Quote.class, quotesCollection);
    }
    public void addQuote(Quote quote){
        mongoTemplate.insert(quote, quotesCollection);
    }

    public void addQuotes(Collection<Quote> quotes){
        mongoTemplate.insert(quotes, quotesCollection);
    }
}
