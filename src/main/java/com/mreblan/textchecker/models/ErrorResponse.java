package com.mreblan.textchecker.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {

	private boolean success;
	private String  message;
}
