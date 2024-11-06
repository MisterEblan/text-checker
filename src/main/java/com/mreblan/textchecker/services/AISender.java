package com.mreblan.textchecker.services;

import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;

public interface AISender {

  Response sendArticle(Article article);
  
}
