package ru.dins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.dins.kafka.synchronization.QuoteInnerSynchronizer;
import ru.dins.kafka.synchronization.QuoteOuterSynchronizer;
import ru.dins.web.persistence.QuoteRepository;


/**
 * Created by gnupinguin on 18.02.17.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        new Thread(context.getBean(QuoteInnerSynchronizer.class)).start();
        new Thread(context.getBean(QuoteOuterSynchronizer.class)).start();

    }
}
