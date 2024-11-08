package com.mreblan.textchecker.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.services.AISender;
import com.mreblan.textchecker.services.ArticleService;
import com.mreblan.textchecker.parsers.Parser;
import com.mreblan.textchecker.parsers.impl.HTMLParser;
import com.mreblan.textchecker.models.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    private final Parser htmlParser;
    private final AISender yandexGptSender;
    
    @Autowired
    public ArticleServiceImpl(HTMLParser htmlParser, YandexGptSender sender) {
      this.htmlParser = htmlParser;
      this.yandexGptSender = sender;
  }

  @Override
  public Response processArticle(Article article) {
    Article cleanedArtical = htmlParser.deleteTags(article);
    log.info("CLEANED ARTICLE: {}", cleanedArtical.toString());

    return yandexGptSender.sendArticle(cleanedArtical);
  }
}
