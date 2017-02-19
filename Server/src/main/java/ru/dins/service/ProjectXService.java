package ru.dins.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dins.model.Quote;
import ru.dins.persistence.ProjectXRepository;

import java.util.List;

/**
 * Created by gnupinguin on 19.02.17.
 */
@Service
public class ProjectXService {
    @Autowired
    ProjectXRepository repository;

    public List<Quote> findAll() {
        return repository.findAll();
    }
    public void insertQuote(Quote quote){repository.addQuote(quote);}
}
