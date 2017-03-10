package ru.dins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


/**
 * Created by gnupinguin on 18.02.17.
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class, args);
    }
}
