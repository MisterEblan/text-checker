package com.mreblan.textchecker.config;

import org.springframework.beans.factory.annotation.Value;

public class OpenAiProperties {

	//@Value("${openai.api.key}")
	public static String API_KEY = System.getenv("OPENAI_API_KEY");
	//@Value("${openai.uri}")
	public static String URI = System.getenv("OPENAI_URI");
}
