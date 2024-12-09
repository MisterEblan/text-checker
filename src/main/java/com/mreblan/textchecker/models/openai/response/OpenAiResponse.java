package com.mreblan.textchecker.models.openai.response;

import com.mreblan.textchecker.models.openai.response.OpenAiUsage;

import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mreblan.textchecker.models.openai.response.OpenAiChoice;

@Data
public class OpenAiResponse {

	private String 			   id;
	private String 			   object;
	private long   			   created;
	private String 			   model;
	private OpenAiUsage 	   usage;
	@JsonProperty("system_fingerprint")
	private String 			   systemFingerprint;
	private List<OpenAiChoice> choices;
}
