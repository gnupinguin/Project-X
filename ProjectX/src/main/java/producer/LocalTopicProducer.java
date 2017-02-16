package producer;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import quote.Quote;

public class LocalTopicProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.62.221:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "quote.QuoteSerializer");

        KafkaProducer<String, Quote> quoteProducer;
        quoteProducer = new KafkaProducer<String, Quote>(props);
        for(int i = 0; i < 10; i++) {
            quoteProducer.send(new ProducerRecord<String, Quote>("Hello", Integer.toString(i), new Quote("Julio","Juliette")));
        }
        quoteProducer.close();
    }
}
