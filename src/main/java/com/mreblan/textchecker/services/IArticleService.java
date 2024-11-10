package com.mreblan.textchecker.services;

import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.Article;

public interface IArticleService {
    
    Response processArticle(Article article);
}
