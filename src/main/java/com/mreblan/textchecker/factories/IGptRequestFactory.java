package com.mreblan.textchecker.factories;

import com.mreblan.textchecker.models.Article;

public interface IGptRequestFactory<T> {
	T createRequest(Article article);
}
