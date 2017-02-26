package ru.dins.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.ProjectXRepository;

import java.util.Collection;
import java.util.List;

/**
 * Created by gnupinguin on 19.02.17.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectXService {
    @Autowired
    private ProjectXRepository repository;

    public List<Quote> findAll() {
        return repository.findAll();
    }

    public void insertQuote(Quote quote){repository.addQuote(quote);}
    public void insertQuotes(Collection<Quote> quotes){
        repository.addQuotes(quotes);
    }

}
