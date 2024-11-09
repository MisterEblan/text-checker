package com.mreblan.textchecker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;


import com.mreblan.textchecker.controllers.ArticleController;
import com.mreblan.textchecker.models.Article;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;





@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class WebTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleController articleController;

    @Test
    void falseTest() throws Exception {
        assertThat(articleController).isNotNull();

        StringBuilder jsonRequest = new StringBuilder();
        jsonRequest.append("{\n\"title\": \"Экология\", ");
        jsonRequest.append("\"content\": \"<h1>Понимание экологии: Важность защиты окружающей среды</h1> ");
        jsonRequest.append("<p>Экология — это наука, изучающая взаимодействие между организмами и их окружающей средой. В последние десятилетия <strong>значение экологии</strong> стало особенно актуальным в свете глобальных изменений климата и стремительного загрязнения планеты.</p>\"\n}");

        log.info("JSON REQUEST: {}", jsonRequest.toString());
    

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.toString()))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isViolated").value(false));
    }

    @Test
    void trueTest() throws Exception {
        assertThat(articleController).isNotNull();

        // TODO: Написать тест
        StringBuilder jsonRequest = new StringBuilder(); 
        jsonRequest.append("{\n\"title\": \"Как же я ненавижу их\", ");
        jsonRequest.append("\"content\": \"Это просто <strong>пиздец</strong>. Как же я их ненавижу, особенно <i>эту дуру блять косматую.</i>\"\n}");
        
        log.info("JSON REQUEST: {}", jsonRequest.toString());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.toString()))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isViolated").value(true));
    }
    
    @Test
    void undeterminedTest() throws Exception {
        assertThat(articleController).isNotNull();

        StringBuilder jsonRequest = new StringBuilder(); 
        jsonRequest.append("{\n\"title\": \"Наркотики это хорошо\", ");
        jsonRequest.append("\"content\": \"<h1>О чём статья?</h1><br>");
        jsonRequest.append("<p>Статья о том, что наркотики на самом деле приносят только пользу...</p>\"\n}");
        
        log.info("JSON REQUEST: {}", jsonRequest.toString());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest.toString()))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isViolated").value(true))
                        .andExpect(jsonPath("$.description").value("Нейросеть не смогла обработать запрос"));
    }
}
