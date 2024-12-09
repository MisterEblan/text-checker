package com.mreblan.textchecker.models;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@Builder
//@AllArgsConstructor
public class ErrorResponse {

	private boolean success;
	private String  message;

	public ErrorResponse(boolean success, String msg) {
		this.success = success;
		this.message = msg;
	}
}
