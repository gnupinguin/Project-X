package ru.dins.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.service.ProjectXService;
import ru.dins.model.quote.Quote;

import java.io.IOException;
import java.util.regex.Pattern;


/**
 * Created by gnupinguin on 19.02.17.
 */
@Controller
public class ProjectXController {
    private static final String ERROR_ADDING_QUOTE_MESSAGE = "When creating quotes error occurred!";
    private static final String SUCCESS_ADDING_QUOTE_MESSAGE = "Quote has been created successfully!";

    private static final Pattern NO_EMPTY_STRING_PATTERN = Pattern.compile("^\\s*$");

    @Autowired
    private ProjectXService projectXService;

    private ProjectXProducer producer;
    {
        try{
            producer = new QuoteProducer("src/main/kafka-conf/innerLocalProducer.properties", "quote-local");
        }catch(IOException e){
            System.out.println("\nNot found properties for producer!\n");
        }
    }

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
            producer.addQuoteInQueue(new Quote(author, quoteText));
            model.addAttribute("message", SUCCESS_ADDING_QUOTE_MESSAGE);
        } catch (Exception e){
            System.out.println(e);
            model.addAttribute("message", ERROR_ADDING_QUOTE_MESSAGE);
        }
        return "status";
    }

    @RequestMapping(value = "/data")
    public String showQuotes(Model model){
        model.addAttribute("quotes", projectXService.findAll());
        return "data";
    }

}
