package com.mreblan.textchecker.models.openai.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mreblan.textchecker.models.openai.OpenAiMessage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenAiChoice {

	private OpenAiMessage message;
	private float         logprobs;
	@JsonProperty("finish_reason")
	private String 		  finishReason;
	private int           index;
}
