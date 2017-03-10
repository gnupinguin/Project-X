package ru.dins.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static ru.dins.LoggersMessageStore.*;

import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.kafka.producer.UnsentQuoteException;
import ru.dins.web.persistence.QuoteRepository;
import ru.dins.web.model.quote.Quote;

import java.net.ConnectException;
import java.util.List;


/**
 * It's controller for preparing client requests.
 */
@Controller @Slf4j
public class ServerController {

    @Autowired
    private QuoteRepository repository;

    @Autowired
    private QuoteProducer producer;

    /**
     * It is used for redirecting to another host, if current server has troubles with kafka or database/
     */
    @Value("${server.remote-host}")
    private String remoteHost;

    /**
     *
     * @return View for start page.
     */
    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    /**
     *
     * @return View for process of creating new quote.
     */
    @RequestMapping("/create")
    public String createQuote(){
        return "create-quote.html";
    }

    /**
     * It trying to send the quote to the local topic kafka server.
     * If {@code text} or {@code author} are not correct text, then the quote will not be sent and will be return {@code ADDING_QUOTE_MESSAGE_ERROR} status.
     * If {@code QuoteProducer} has some troubles, then request will be redirected to another server.
     * @param text Text of quote
     * @param author Author of quote
     * @param model Model for view-response.
     * @return View with status of sending quote to local topic.
     */
    @RequestMapping(value="/add")
    public String addQuote(@RequestParam(value = "quote") String text,
                           @RequestParam(value = "author") String author,
                           Model model){
        try{
            text = text.trim();
            author = author.trim();
            if (text.equals("") || author.equals("")){
                model.addAttribute("message", ADDING_QUOTE_MESSAGE_ERROR);
                return "status";
            }
            producer.addQuote2LocalTopic(new Quote(author, text));
            model.addAttribute("message", SUCCESS_ADDING_QUOTE_MESSAGE);
        } catch (UnsentQuoteException e){
            log.error(e.getMessage());
            return String.format("redirect:%s/add?quote=%s&author=%s", remoteHost, text, author);
        }
        return "status";
    }

    /**
     * It trying to find all quotes in database.
     * If connection to database is not available, then request will be redirected to another host.
     * @param model Model for view-response.
     * @return  View with all quotes in database.
     */
    @RequestMapping(value = "/data")
    public String showQuotes(Model model){
        List<Quote> quotes = null;
        try{
            quotes = repository.findAll();
            model.addAttribute("quotes",  quotes);
            return "data";
        } catch (ConnectException e){
            log.warn(REPOSITORY_CONNECTION_FAILED_WARNING);
            return "redirect:" + remoteHost + "/data";
        }

    }

}
