package com.mreblan.textchecker.services;

import com.mreblan.textchecker.exceptions.BadResponseException;
import com.mreblan.textchecker.models.Article;

public interface IAiSender {

  String sendArticle(Article article) throws BadResponseException;
  
}
