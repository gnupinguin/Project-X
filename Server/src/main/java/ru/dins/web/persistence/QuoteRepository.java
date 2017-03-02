package ru.dins.web.persistence;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.dins.web.model.quote.Quote;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.List;
import java.util.UnknownFormatFlagsException;


/**
 * Created by gnupinguin on 19.02.17.
 */
@Repository @NoArgsConstructor
public class QuoteRepository {

    @Value("${spring.data.mongodb.collection}")
    private String quotesCollection;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Socket socket4Db;

    public List<Quote> findAll() {
        return mongoTemplate.findAll(Quote.class, quotesCollection);
    }
    public void addQuote(Quote quote) {
        mongoTemplate.insert(quote, quotesCollection);
    }


    public boolean availableConnection() throws UnknownFormatFlagsException {
       try{
           socket4Db = new Socket(InetAddress.getByName(host), port);
           return true;
       } catch (Exception e){
           return false;
       }

    }
}
