package com.mreblan.textchecker.models.yandexgpt;

import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class YandexGptMessage {
    String role;
    String text;
}
