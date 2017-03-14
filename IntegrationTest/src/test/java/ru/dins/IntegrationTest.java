package ru.dins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.dins.web.model.quote.Quote;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by key on 09.03.2017.
 */
//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest {
    @Value("${test.first-host}")
    String firstHost;
    @Value("${test.second-host}")
    String secondHost;
    RestTemplate restTemplate = new RestTemplate();
    Quote testQuote = new Quote("TestAuthor", "TestQuote");
    @Autowired
    MongoTemplate mongoTemplate;
    private final static int AUTHOR_GROUP = 2;
    private final static int TEXT_GROUP = 1;
    @Test
    public void IntegrationTest() throws Exception{

       restTemplate.getForObject(secondHost + "/add?quote={TestQuote}&author={TestAuthor}",
                String.class,testQuote.getText(),testQuote.getAuthor());
        String responseFromFirstHost = restTemplate.getForObject(firstHost + "/data", String.class);
        String responseFromSecondHost = restTemplate.getForObject(secondHost + "/data", String.class);
        Pattern pattern = Pattern.compile(" <div class=\"media\">\\s*" +
                "<div class=\"media-body\">\\s*" +
                " <p>(.+)</p>\\s*" +
                "<h4 class=\"media-heading\" align=\"right\">(.+)</h4>\\s*" +
                " </div>\\s*" +
                "</div>\\s*" +
                "<hr>");
        Matcher matcherForFirstHost = pattern.matcher(responseFromFirstHost);
        Matcher matcherForSecondHost = pattern.matcher(responseFromSecondHost);
        boolean quoteInFirstHostFound = false;
        boolean quoteInSecondHostFound = false;

        while(matcherForFirstHost.find() && !quoteInFirstHostFound){
            if (testQuote.equals(new Quote(matcherForFirstHost.group(AUTHOR_GROUP),matcherForFirstHost.group(TEXT_GROUP))))
                quoteInFirstHostFound = true;
        }
        while(matcherForSecondHost.find() && !quoteInSecondHostFound){
            if (testQuote.equals(new Quote(matcherForSecondHost.group(AUTHOR_GROUP),matcherForSecondHost.group(TEXT_GROUP))))
                quoteInSecondHostFound = true;
        }
        assertThat(quoteInFirstHostFound && quoteInSecondHostFound).isTrue();
    }
}