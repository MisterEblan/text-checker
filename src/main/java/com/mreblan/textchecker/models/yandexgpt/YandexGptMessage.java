package com.mreblan.textchecker.models.yandexgpt;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class YandexGptMessage {
    String role;
    String text;
}
