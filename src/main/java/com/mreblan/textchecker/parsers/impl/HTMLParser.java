package com.mreblan.textchecker.parsers.impl;

import com.mreblan.textchecker.parsers.Parser;

import lombok.extern.slf4j.Slf4j;

import com.mreblan.textchecker.models.Article;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HTMLParser implements Parser {

  @Autowired
  public HTMLParser() {}

  @Override
  public Article deleteTags(Article article) {
    String content = article.getContent();
    String cleanedContent = Jsoup.clean(content, Safelist.none());

    log.info("CLEANED CONTENT: {}", cleanedContent);
    return new Article(article.getTitle(), cleanedContent);
  }
}
