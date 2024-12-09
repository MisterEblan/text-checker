package com.mreblan.textchecker.models;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Article {
	@JsonProperty("title")
    private String title;
	@JsonProperty("content")
    private String content;
}
