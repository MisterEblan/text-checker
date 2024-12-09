package com.mreblan.textchecker.models.openai.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenAiUsage {

	@JsonProperty("prompt_tokens")
	private int promptTokens;
	@JsonProperty("completion_tokens")
	private int completionTokens;
	@JsonProperty("total_tokens")
	private int totalTokens;
}
