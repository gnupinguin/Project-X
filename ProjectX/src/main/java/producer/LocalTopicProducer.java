package producer;
import java.io.*;
import java.util.Properties;


public class LocalTopicProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        try
        {
            FileInputStream fis = new FileInputStream("target/config/local-producer/producer.properties");
            props.load(fis);
        }
        catch (IOException e)
        {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        ImplementProducer.addOuotesInQueue("quote-local", args, props);
        System.out.printf("Finish!\n");
    }
}
