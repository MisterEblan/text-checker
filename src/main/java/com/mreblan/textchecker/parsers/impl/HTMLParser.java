package com.mreblan.textchecker.parsers.impl;

import com.mreblan.textchecker.parsers.IParser;

import lombok.extern.slf4j.Slf4j;

import com.mreblan.textchecker.models.Article;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HTMLParser implements IParser {

  @Autowired
  public HTMLParser() {}

  // Метод для очистки контента от HTML-тегов
  @Override
  public Article deleteTags(Article article) {
    // Получаем контент статьи
    String content = article.getContent();
    // С помощью библиотеки JSOUP
    // очищаем контент
    String cleanedContent = Jsoup.clean(content, Safelist.none());

    // Логируем очищенный контент
    log.info("CLEANED CONTENT: {}", cleanedContent);

    return new Article(article.getTitle(), cleanedContent);
  }
}
