package com.mreblan.textchecker.parsers;

import org.springframework.stereotype.Component;

import com.mreblan.textchecker.models.Article;

@Component
public interface IParser {

    Article deleteTags(Article article);
}