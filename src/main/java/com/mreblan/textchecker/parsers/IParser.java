package com.mreblan.textchecker.parsers;

import com.mreblan.textchecker.models.Article;

public interface IParser {

    Article deleteTags(Article article);
}
