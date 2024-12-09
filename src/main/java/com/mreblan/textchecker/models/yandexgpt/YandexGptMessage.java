package com.mreblan.textchecker.models.yandexgpt;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
public class YandexGptMessage {
    String role;
    String text;

    public YandexGptMessage(String role, String text) {
        this.role = role;
        this.text = text;
    }
}
