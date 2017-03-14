package ru.dins.web.persistence;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * Simple alternative for {@link MongoTemplate}
 * Properties {@code host}, {@code port} and {@code databaseName} are in application.yml.
 * After {@code serverSelectionTimeout} ms waiting connection to MongoDB server, an exception will be thrown.
 * The option is application.yml
 */
@Configuration @Data @NoArgsConstructor
@Profile({"test"})
public class MongoConfigTest {
    @NonNull @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @NonNull @Value("${spring.inegrationtest.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.time-wait}")
    private int serverSelectionTimeout;

    /**
     *
     * @return Bean of{@link MongoDbFactory} with option {@code serverSelectionTimeout} for throwing time wait exception.
     */
    public @Bean
    MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(
                new MongoClient(host,
                        MongoClientOptions.
                                builder().
                                serverSelectionTimeout(serverSelectionTimeout).build()),
                databaseName);
    }

    @Bean
    public MongoTemplate anotherMongoTemplate() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;

    }
}
