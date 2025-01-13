package ru.clevertec.adapter.input.web.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.clevertec.adapter.input.web.news.dto.NewsRequest;
import ru.clevertec.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.adapter.input.web.news.dto.NewsUpdateRequest;
import ru.clevertec.exception.NewsNotFoundException;
import ru.clevertec.port.input.news.CreateNewsUseCase;
import ru.clevertec.port.input.news.DeleteNewsUseCase;
import ru.clevertec.port.input.news.ReadNewsUseCase;
import ru.clevertec.port.input.news.UpdateNewsUseCase;
import ru.clevertec.port.input.news.command.NewsCreateCommand;
import ru.clevertec.port.input.news.command.NewsUpdateCommand;
import ru.clevertec.port.input.news.command.NewsUseCaseResult;
import ru.clevertec.util.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateNewsUseCase createNewsUseCase;

    @MockitoBean
    ReadNewsUseCase readNewsUseCase;

    @MockitoBean
    UpdateNewsUseCase updateNewsUseCase;

    @MockitoBean
    DeleteNewsUseCase deleteNewsUseCase;

    @MockitoBean
    WebNewsMapper newsMapper;

    @Test
    void shouldReadAllNews() throws Exception {
        //given
        int pageNumber = 0;
        int pageSize = 3;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<NewsUseCaseResult> newsUseCaseResults = TestData.generateListOfNewsUseCaseResults();

        Page<NewsUseCaseResult> newsUseCaseResultsPage =
                new PageImpl<>(newsUseCaseResults.subList(0, 3), pageRequest, newsUseCaseResults.size());

        when(readNewsUseCase.readAllNews(pageNumber, pageSize))
                .thenReturn(newsUseCaseResultsPage);

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/news")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
                .with(user("Nekitos4").roles("ADMIN"));


        //when, then
        mockMvc.perform(requestBuilder)
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.length()").value(3)
                );
    }


    @Test
    void shouldReadNewsById_whenNewsExist() throws Exception {
        //given
        NewsResponse newsResponse = TestData.generateNewsResponse();
        NewsUseCaseResult newsUseCaseResult = TestData.generateNewsUseCaseResult();
        UUID newsId = newsResponse.id();

        when(readNewsUseCase.readNewsById(newsId))
                .thenReturn(newsUseCaseResult);
        when(newsMapper.toNewsResponse(newsUseCaseResult))
                .thenReturn(newsResponse);

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/news/{id}", newsId)
                .with(user("Nekitos4").roles("ADMIN"));
        //.with(jwt().jwt(jwt -> jwt.claim("scope", "view_news")));

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpectAll(status().isOk(),
                        jsonPath("$.id").value(newsId.toString()),
                        jsonPath("$.title").value(newsResponse.title()),
                        jsonPath("$.text").value(newsResponse.text()),
                        jsonPath("$.authorId").value(newsResponse.authorId().toString())
                );
    }

    @Test
    void shouldNotReadNewsById_whenNewsNotExist() throws Exception {
        //given
        UUID newsId = UUID.randomUUID();

        when(readNewsUseCase.readNewsById(newsId))
                .thenThrow(NewsNotFoundException.class);

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/news/{id}", newsId)
                .with(user("Nekitos4").roles("ADMIN"));

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }


    @Test
    void shouldCreateNews() throws Exception {
        //given
        UUID authorId = UUID.randomUUID();
        NewsRequest newsRequest = TestData.generateNewsRequest();
        NewsCreateCommand newsCreateCommand = TestData.generateNewsCreateCommand();
        NewsUseCaseResult newsUseCaseResult = TestData.generateNewsUseCaseResult();
        NewsResponse newsResponse = TestData.generateNewsResponse();

        when(newsMapper.toNewsCreateCommand(newsRequest))
                .thenReturn(newsCreateCommand);
        when(createNewsUseCase.createNews(newsCreateCommand, authorId))
                .thenReturn(newsUseCaseResult);
        when(newsMapper.toNewsResponse(newsUseCaseResult))
                .thenReturn(newsResponse);

        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsRequest))
                .with(SecurityMockMvcRequestPostProcessors.user(new User(authorId.toString(), "",
                        new ArrayList<>())))
                .with(csrf());

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsResponse.id().toString()))
                .andExpect(jsonPath("$.title").value(newsResponse.title()))
                .andExpect(jsonPath("$.text").value(newsResponse.text()))
                .andExpect(jsonPath("$.authorId").value(newsResponse.authorId().toString()));
    }

    @Test
    void shouldUpdateNews() throws Exception {
        //given
        NewsResponse newsResponse = TestData.generateNewsResponse();
        NewsUseCaseResult newsUseCaseResult = TestData.generateNewsUseCaseResult();
        NewsUpdateCommand newsUpdateCommand = TestData.generateNewsUpdateCommand();
        NewsUpdateRequest newsUpdateRequest = TestData.generateNewsUpdateRequest();

        when(newsMapper.toNewsUpdateCommand(newsUpdateRequest))
                .thenReturn(newsUpdateCommand);
        when(updateNewsUseCase.updateNews(newsUpdateCommand))
                .thenReturn(newsUseCaseResult);
        when(newsMapper.toNewsResponse(newsUseCaseResult))
                .thenReturn(newsResponse);


        MockHttpServletRequestBuilder requestBuilder = put("/api/v1/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsUpdateRequest))
                .with(user("Nekitos4").roles("ADMIN"))
                .with(csrf());

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsResponse.id().toString()))
                .andExpect(jsonPath("$.title").value(newsResponse.title()))
                .andExpect(jsonPath("$.text").value(newsResponse.text()))
                .andExpect(jsonPath("$.authorId").value(newsResponse.authorId().toString()));
    }

    @Test
    void shouldDeleteById() throws Exception {
        //given
        UUID newsId = UUID.randomUUID();

        MockHttpServletRequestBuilder requestBuilder =
                delete("/api/v1/news/{id}", newsId)
                        .with(user("Nekitos4").roles("ADMIN"))
                        .with(csrf());
        //when, then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }
}