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


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;





@SpringBootTest
@AutoConfigureMockMvc
public class WebTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ArticleController articleController;

    @Test
    void contextLoads() throws Exception {
        assertThat(articleController).isNotNull();

        String jsonRequest = "{\"title\": \"Healthy food\", \"content\": \"<p>Eating <i>healthy</i> is <strong>extremely important.<br></strong></p>\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.violated").value(true));

  }
}
