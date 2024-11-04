package com.mreblan.textchecker.services.impl;

import org.springframework.stereotype.Service;

import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.services.ArticleService;

import lombok.extern.slf4j.Slf4j;

import com.mreblan.textchecker.parsers.Parser;
import com.mreblan.textchecker.parsers.impl.HTMLParser;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    Parser htmlParser = new HTMLParser();
    
    Response processArticle(Article article) {
        Article cleanedArticle = htmlParser.deleteTags(article);
        
        return new Response(true, "Tags");
    }
}
