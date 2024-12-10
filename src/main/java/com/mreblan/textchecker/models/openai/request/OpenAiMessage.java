package com.mreblan.textchecker.models.openai.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenAiMessage {
	private String role;
	private String content;
}
