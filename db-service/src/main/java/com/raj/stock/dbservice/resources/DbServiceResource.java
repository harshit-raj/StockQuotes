package com.raj.stock.dbservice.resources;

import com.raj.stock.dbservice.model.Quote;
import com.raj.stock.dbservice.model.Quotes;
import com.raj.stock.dbservice.repository.QuotesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/db")
public class DbServiceResource {

    private QuotesRepository quotesRepository;

    public DbServiceResource(QuotesRepository quotesRepository){
        this.quotesRepository = quotesRepository;
    }

    @GetMapping("/{username}")
    public List<String> getQuotes(@PathVariable("username") final  String username){


        return getQuotesByUserName(username);


    }

    private List<String> getQuotesByUserName(@PathVariable("username") String username) {
        return quotesRepository.findByUserName(username)
        .stream()
        .map(Quote:: getQuote)
        .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public List<String> add(@RequestBody final Quotes quotes){

//        quotes.getQuotes()
//                .stream()
//                .forEach((quote -> {
//                    quotesRepository.save(new Quote(quotes.getUserName(),quote));
//                }));
//
        quotes.getQuotes()
                .stream()
                .map(quote -> new Quote(quotes.getUserName(),quote))
                .forEach((quote -> {
                    quotesRepository.save(quote);
                }));
        return getQuotesByUserName(quotes.getUserName());
    }

    @PostMapping("/delete/{username}")
    public List<String> delete(@PathVariable("username") final String username){
        List<Quote> qts = quotesRepository.findByUserName(username);
        quotesRepository.deleteAll(qts);
        return getQuotesByUserName(username);
    }


}
