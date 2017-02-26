package ru.dins;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.kafka.synchronization.QuoteLocalSynchronizer;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.service.ProjectXService;

import java.util.List;

/**
 * Created by gnupinguin on 18.02.17.
 */
@SpringBootApplication
@ImportResource({"classpath*:KafkaFilesConfigurationContext.xml"})
public class Application {
    public static void main(String[] args) throws Exception {

//
//        new Thread(quoteLocalSynchronizer).start();
//        new Thread(quoteOuterSynchronizer).start();

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        new Thread(context.getBean(QuoteLocalSynchronizer.class)).start();

    }
}
