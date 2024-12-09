package com.mreblan.textchecker.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mreblan.textchecker.models.Response;
import com.mreblan.textchecker.services.IArticleService;
import com.mreblan.textchecker.services.impl.ArticleServiceImpl;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.mreblan.textchecker.exceptions.IllegalGptTypeException;
import com.mreblan.textchecker.models.Article;
import com.mreblan.textchecker.models.ErrorResponse;
import com.mreblan.textchecker.models.GptType;

@OpenAPIDefinition(info = @Info(title = "Article Checker API", version = "v1"))
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final IArticleService service;

	@Operation(summary = "Проверка статьи",
        description = "Отправляет статью на проверку нейросети YandexGPT"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200",
            description = "Всё прошло удачно",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        example = "{ \"isViolated\": false, \"description\": \"Статья соответствует правилам публикации\"}"
                    )
                )
            ),
            @ApiResponse(responseCode = "500",
            description = "При отправке запроса что-то пошло не так"
            )
        }
    )
    @PostMapping("/yandex")
    public ResponseEntity<?> yandexCheckArticle(@RequestBody Article article) {
        log.info(article.toString());

		Response resp = null;
		try {
			resp = service.processArticle(article, GptType.YANDEX);
		} catch (IllegalGptTypeException e) {
			log.error(e.getMessage());

			ErrorResponse err = ErrorResponse.builder()
									.success(false)
									.message("Неправильно указан тип GPT")
									.build();

			return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(err);
		}

		return ResponseEntity.ok(resp);
    }



	@Operation(summary = "Проверка статьи",
        description = "Отправляет статью на проверку нейросети OpenAI"
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200",
            description = "Всё прошло удачно",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        example = "{ \"isViolated\": false, \"description\": \"Статья соответствует правилам публикации\"}"
                    )
                )
            ),
            @ApiResponse(responseCode = "500",
            description = "При отправке запроса что-то пошло не так"
            )
        }
    )
    @PostMapping("/openai")
    public ResponseEntity<?> openAiCheckArticle(@RequestBody Article article) {
        log.info(article.toString());

		Response response = null;
		try {
			response = service.processArticle(article, GptType.OPEN_AI);
		} catch (IllegalGptTypeException e) {
			log.error(e.getMessage());

			ErrorResponse err = ErrorResponse.builder()
									.success(false)
									.message("Неправильно указан тип GPT")
									.build();

			return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(err);
		}

		if (response == null) {
			ErrorResponse errResponse = ErrorResponse.builder()
											.success(false)
											.message("Что-то пошло не так")
											.build();

			return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(errResponse);
		}

		return ResponseEntity.ok(response);

    }
}
