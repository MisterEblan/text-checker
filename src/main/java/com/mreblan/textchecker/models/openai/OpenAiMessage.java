package com.mreblan.textchecker.models.openai;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenAiMessage {

	private String role;
	private String content;
}
