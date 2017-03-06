package ru.dins.web.persistence;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import ru.dins.web.model.quote.Quote;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.UnknownFormatFlagsException;


/**
 * Created by gnupinguin on 19.02.17.
 */
@Repository @NoArgsConstructor
@ImportResource({"classpath*:ApplicationContext.xml"})
public class QuoteRepository {

    @Value("${spring.data.mongodb.collection}")
    private String quotesCollection;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

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


    public boolean availableConnection() {
       try{
           new Socket(InetAddress.getByName(host), port).close();
           return true;
       } catch (Exception e){
           return false;
       }

    }
}
