package com.mreblan.textchecker.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.services.IAiSender;
import com.mreblan.textchecker.services.IArticleService;
import com.mreblan.textchecker.parsers.IParser;
import com.mreblan.textchecker.parsers.impl.HTMLParser;
import com.mreblan.textchecker.models.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleServiceImpl implements IArticleService {
    private final IParser htmlParser;
    private final IAiSender yandexGptSender;
    
    @Autowired
    public ArticleServiceImpl(HTMLParser htmlParser, YandexGptSender sender) {
      this.htmlParser = htmlParser;
      this.yandexGptSender = sender;
  }

  @Override
  public Response processArticle(Article article) {
    Article cleanedArtical = htmlParser.deleteTags(article);
    // Дополнительно логируем очищенный контент
    // Чтобы убедиться, что всё прошло нормально
    log.info("CLEANED ARTICLE: {}", cleanedArtical.toString());

    // Передаём очищенный контент
    // в нейросеть и получаем оттуда ответ
    return yandexGptSender.sendArticle(cleanedArtical);
  }
}
