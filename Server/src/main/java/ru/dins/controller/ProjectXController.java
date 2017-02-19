package ru.dins.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dins.model.Quote;
import ru.dins.service.ProjectXService;


/**
 * Created by gnupinguin on 19.02.17.
 */
@Controller
public class ProjectXController {
    private ProjectXService projectXService;

    private static final String ERROR_MESSAGE = "When creating quotes error occurred!";
    private static final String SUCCESS_MESSAGE = "Quote has been created successfully!";

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
            projectXService.insertQuote(new Quote(quotestext, author));
            model.addAttribute("message", SUCCESS_MESSAGE);
        } catch (Exception e){
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
