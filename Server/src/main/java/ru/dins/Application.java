package ru.dins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import ru.dins.kafka.synchronization.QuoteLocalSynchronizer;
import ru.dins.kafka.synchronization.QuoteOuterSynchronizer;


/**
 * Created by gnupinguin on 18.02.17.
 */
@SpringBootApplication
@ImportResource({"classpath*:KafkaFilesConfigurationContext.xml"})
public class Application {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        new Thread(context.getBean(QuoteLocalSynchronizer.class)).start();
        new Thread(context.getBean(QuoteOuterSynchronizer.class)).start();

    }
}
