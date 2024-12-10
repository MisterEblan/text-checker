package com.mreblan.textchecker.models.openai.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenAiResponse {

	private String 			   id;
	private String 			   object;
	private long   			   created;
	private String 			   model;
	@JsonProperty("system_fingerprint")
	private String 			   systemFingerprint;
	private List<OpenAiChoice> choices;
	private OpenAiUsage 	   usage;
}
