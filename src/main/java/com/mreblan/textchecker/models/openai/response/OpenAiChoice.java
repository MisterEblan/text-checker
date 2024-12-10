package com.mreblan.textchecker.models.openai.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mreblan.textchecker.models.openai.request.OpenAiMessage;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenAiChoice {

	private int index;
	private OpenAiMessage message;
	private float logprobs;
	@JsonProperty("finish_reason")
	private String finishReason;
}
