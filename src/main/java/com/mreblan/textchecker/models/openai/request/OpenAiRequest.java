package com.mreblan.textchecker.models.openai.request;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenAiRequest {

	private String model;
	private List<OpenAiMessage> messages;
	private float temperature;
}
