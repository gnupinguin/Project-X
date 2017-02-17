package producer;
import quote.Quote;
import org.apache.kafka.clients.producer.KafkaProducer;
import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by dins on 16.02.17.
 */
public class ImplementProducer
{
    static void  addOuotesInQueue(String topicName, String[] quotes, Properties props)
    {
        KafkaProducer<String, Quote> quoteProducer;
        quoteProducer = new KafkaProducer<String, Quote>(props);
        for(int i = 0; i < quotes.length / 2; i++)
        {
            quoteProducer.send(new ProducerRecord<String, Quote>(topicName, Integer.toString(i*2), new Quote(quotes[i*2],quotes[i*2+1])));
            // topic and key not in final version
        }

        quoteProducer.close();
    }
}
