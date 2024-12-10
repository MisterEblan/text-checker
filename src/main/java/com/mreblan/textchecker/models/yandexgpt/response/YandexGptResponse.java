package com.mreblan.textchecker.models.yandexgpt.response;

import java.util.List;

import com.mreblan.textchecker.models.yandexgpt.YandexGptMessage;

import lombok.Data;

@Data
public class YandexGptResponse {
    private Result result;

    @Data
    public static class Result {
        private List<Alternative> alternatives;
        private Usage usage;
        private String modelVersion;

        @Data
        public static class Alternative {
            private YandexGptMessage message;
            private String status;

        }
    }

    @Data
    public static class Usage {
        private String inputTextTokens;
        private String completionTokens;
        private String totalTokens;
    }
}
