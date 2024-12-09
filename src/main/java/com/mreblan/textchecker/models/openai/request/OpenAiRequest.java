package com.mreblan.textchecker.models.openai.request;

import java.util.List;

import com.mreblan.textchecker.models.openai.OpenAiMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenAiRequest {

	private String model;
	private List<OpenAiMessage> messages;
	private float temperature;
}
