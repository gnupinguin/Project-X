package ru.dins.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.web.persistence.QuoteRepository;
import ru.dins.web.model.quote.Quote;

import java.util.regex.Pattern;


/**
 * Created by gnupinguin on 19.02.17.
 */

@Controller
public class ServerController {
    private static final String ERROR_ADDING_QUOTE_MESSAGE = "When creating quotes error occurred!";
    private static final String SUCCESS_ADDING_QUOTE_MESSAGE = "Quote has been created successfully!";

    private static final Pattern NO_EMPTY_STRING_PATTERN = Pattern.compile("^\\s*$");

    @Autowired
    private QuoteRepository repository;

    @Autowired
    private QuoteProducer producer;

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/create")
    public String createQuote(){
        return "create-quote.html";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String addQuote(@RequestParam(value = "quote") String quoteText,
                           @RequestParam(value = "author") String author,
                           Model model){
        try{

            quoteText = quoteText.trim();
            author = author.trim();

            if (NO_EMPTY_STRING_PATTERN.matcher(quoteText).find() || NO_EMPTY_STRING_PATTERN.matcher(author).find())
                throw new RuntimeException();
            producer.addQuote2MainPartitionLocalTopic(new Quote(author, quoteText));
            model.addAttribute("message", SUCCESS_ADDING_QUOTE_MESSAGE);
        } catch (Exception e){
            System.out.println(e);
            model.addAttribute("message", ERROR_ADDING_QUOTE_MESSAGE);
        }
        return "status";
    }

    @RequestMapping(value = "/data")
    public String showQuotes(Model model){
        model.addAttribute("quotes", repository.findAll());
        return "data";
    }

}
