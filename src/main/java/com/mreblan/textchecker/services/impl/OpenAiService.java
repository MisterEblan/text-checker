package com.mreblan.textchecker.services.impl;

import com.mreblan.textchecker.services.IAiSender;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.models.openai.request.OpenAiRequest;
import com.mreblan.textchecker.models.openai.response.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mreblan.textchecker.config.OpenAiProperties;
import com.mreblan.textchecker.exceptions.UnsuccessfulRequestException;
import com.mreblan.textchecker.factories.impl.OpenAiRequestFactoryImpl;
import com.mreblan.textchecker.models.Article;

@Slf4j
//@AllArgsConstructor
//@RequiredArgsConstructor
@Service
public class OpenAiService implements IAiSender {
	
	private final RestClient 			   restClient;
	private final OpenAiRequestFactoryImpl requestFactory;
	private final ObjectMapper             objMapper;

	public OpenAiService(RestClient client, OpenAiRequestFactoryImpl rFactory, ObjectMapper objMapper) {
		this.restClient = client;
		this.requestFactory = rFactory;
		this.objMapper = objMapper;
	}

	@Override
	public Response sendArticle(Article article) {
		OpenAiRequest request = requestFactory.createRequest(article);

		//log.info("OPENAI REQUEST: {}", request.toString());
		//
		log.info("URI: {}", OpenAiProperties.URI);

		ResponseEntity<OpenAiResponse> response = restClient.post().uri(OpenAiProperties.URI)
														.header("Authorization", "Bearer %s".formatted(OpenAiProperties.API_KEY))
														.contentType(MediaType.APPLICATION_JSON)
														.body(request)
														.retrieve()
														.toEntity(OpenAiResponse.class);

		//log.info("OPENAI RESPONSE: {}", response.toString());

		if (!response.getStatusCode().is2xxSuccessful()) {
			//log.error("RESPONSE WAS NOT SUCCESSFUL: {}", response.getStatusCode());
			throw new UnsuccessfulRequestException("Request to OpenAI was not successful");
		}

		String responseContent = response.getBody().getChoices().get(0).getMessage().getContent();

		//log.info("RESPONSE CONTENT: {}", responseContent);

		Response finalResponse = null;

		try {
			finalResponse = jsonToResponse(responseContent);

			return finalResponse;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Response jsonToResponse(String json) throws JsonProcessingException {
		json = json.replace("`", "");

		Map<String, Object> tempMap = objMapper.readValue(json, new TypeReference<Map<String, Object>>() {});

		return new Response(
			(Boolean) tempMap.get("isViolated"),
			(String) tempMap.get("description")
		);
	}
}
