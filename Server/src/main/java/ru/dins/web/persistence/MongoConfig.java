package ru.dins.web.persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * Created by gnupinguin on 06.03.17.
 */
@Configuration @Data @NoArgsConstructor
public class MongoConfig {
    @NonNull @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @NonNull @Value("${spring.data.mongodb.database}")
    private String databaseName;

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(
                new MongoClient(host,
                        MongoClientOptions.
                                builder().
                                serverSelectionTimeout(1000).build()),
                databaseName);
    }

    @Bean
    public MongoTemplate anotherMongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;

    }
}
