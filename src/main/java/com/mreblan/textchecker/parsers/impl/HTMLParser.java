package com.mreblan.textchecker.parsers.impl;

import org.jsoup.Jsoup;
import org.jsoup.WhiteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mreblan.textchecker.models.Article;

import lombok.Data;

@Data
@Component
public class HTMLParser implements Parser {
    
    @Autowired
    public HTMLParser() {}

    Article deleteTags(Article article) {
        String content = article.getContent();
        String cleanedContent = Jsoup.clean(content, WhiteList.none());

        return new Article(article.getTitle(), cleanedContent);
    }
}
