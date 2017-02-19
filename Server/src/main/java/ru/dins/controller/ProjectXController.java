package ru.dins.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dins.model.Quote;
import ru.dins.service.ProjectXService;

import java.util.regex.Pattern;


/**
 * Created by gnupinguin on 19.02.17.
 */
@Controller
public class ProjectXController {
    @Autowired
    private ProjectXService projectXService;

    private static final String ERROR_MESSAGE = "When creating quotes error occurred!";
    private static final String SUCCESS_MESSAGE = "Quote has been created successfully!";

    private static final Pattern NO_EMPTY_STRING_PATTERN = Pattern.compile("^\\s*$");

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/create")
    public String createQuote(){
        return "create-quote.html";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String addQuote(@RequestParam(value = "quote") String quotestext,
                           @RequestParam(value = "author") String author,
                           Model model){

        try{
            if (NO_EMPTY_STRING_PATTERN.matcher(quotestext).find() || NO_EMPTY_STRING_PATTERN.matcher(author).find())
                throw new RuntimeException();

            projectXService.insertQuote(new Quote(quotestext, author));
            model.addAttribute("message", SUCCESS_MESSAGE);
        } catch (Exception e){
            System.out.println(e);
            model.addAttribute("message", ERROR_MESSAGE);
        }
        return "status";
    }

    @RequestMapping(value = "/data")
    public String showQuotes(Model model){
        model.addAttribute("quotes", projectXService.findAll());
        return "data";
    }

}
