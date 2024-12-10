package com.mreblan.textchecker.models;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
@Component
public class Response {
    @JsonProperty("isViolated")
    private boolean isViolated;
    @JsonProperty("description")
    private String  description;
}
