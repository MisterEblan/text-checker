package com.mreblan.textchecker.services.impl;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mreblan.textchecker.config.OpenAiProperties;
import com.mreblan.textchecker.exceptions.BadResponseException;
import com.mreblan.textchecker.factories.IGptRequestFactory;
import com.mreblan.textchecker.factories.impl.OpenAiRequestFactoryImpl;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.openai.request.OpenAiMessage;
import com.mreblan.textchecker.models.openai.request.OpenAiRequest;
import com.mreblan.textchecker.models.openai.response.OpenAiResponse;
import com.mreblan.textchecker.services.IAiSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OpenAiService implements IAiSender {

	private final RestClient restClient;
	private final IGptRequestFactory<OpenAiRequest> openAiRequestFactory;

	@Override
	public String sendArticle(Article article) throws BadResponseException {

		OpenAiRequest request = openAiRequestFactory.createRequest(article);

		log.info("REQUEST: {}", request.toString());

		ResponseEntity<OpenAiResponse> response =  restClient.post()
														.uri(OpenAiProperties.URI)
														.header("Authorization", "Bearer %s".formatted(OpenAiProperties.API_KEY))
														.contentType(MediaType.APPLICATION_JSON)
														.body(request)
														.retrieve()
														.onStatus(HttpStatusCode::is4xxClientError, (_request, _response) -> {
															log.error("Русня");
															log.error(_response.getStatusText());
															throw new BadResponseException("русня");
														})
														.toEntity(OpenAiResponse.class);

		log.info("RESPONSE: {}", response.toString());

		OpenAiMessage responseMessage = response.getBody().getChoices().get(0).getMessage();

		return responseMessage.getContent();
	}
}
