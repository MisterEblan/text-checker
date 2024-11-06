package com.mreblan.textchecker.models.yandexgpt;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class YandexGptMessage {
    String role;
    String text;
}
