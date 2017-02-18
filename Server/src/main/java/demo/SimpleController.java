package demo;

import com.mongodb.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gnupinguin on 18.02.17.
 */
@Controller
public class SimpleController {
    private static final String ERROR_MESSAGE = "When creating quotes error occurred!";
    private static final String SUCCESS_MESSAGE = "Quote has been created successfully!";

    private DBCollection quotesCollection;

    SimpleController(){
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        DB db = mongoClient.getDB( "QuotesDB" );
        quotesCollection = db.getCollection("quotes");
    }
    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/create")
    public String createQuote(){
        return "create-quote.html";
    }

    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String addQuote(@RequestParam(value = "quote") String quote,
                           @RequestParam(value = "author") String author,
                           Model model){
        try{
            quotesCollection.insert(new BasicDBObject("author", author).append("quotestext", quote));
        }catch (Exception e){
            model.addAttribute("message", ERROR_MESSAGE);
            return "status";
        }
        model.addAttribute("message", SUCCESS_MESSAGE);
        return "status";
    }

    @RequestMapping(value = "/data")
    public String showQuotes(Model model){
        Map<String, String> quotes = new HashMap<>();
        DBCursor cursor  = quotesCollection.find();
        try {
            while(cursor.hasNext()) {
                DBObject obj = cursor.next();
                quotes.put((String)obj.get("quotestext"), (String)obj.get("author"));
            }
        } finally {
            cursor.close();
        }
        model.addAttribute("quotes", quotes);
        return "data";
    }
}
