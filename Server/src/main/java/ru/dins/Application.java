package ru.dins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import ru.dins.kafka.producer.KafkaQuoteProducer;
import ru.dins.web.model.quote.Quote;


/**
 * Created by gnupinguin on 18.02.17.
 */
@SpringBootApplication
@ImportResource({"classpath*:ApplicationContext.xml"})
public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        KafkaQuoteProducer p = context.getBean(KafkaQuoteProducer.class);
        //p.addQuote2LocalTopic(new Quote("Кино", "А тем кто ложиться спать - спокойного сна."));
        //p.addQuote2ReserveTopic(new Quote("Purgen", "Кристально чистая ночь, проведённая только с тобой"));
    }
}
