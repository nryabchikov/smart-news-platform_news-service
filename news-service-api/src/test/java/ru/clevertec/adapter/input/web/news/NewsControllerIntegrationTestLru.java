package ru.clevertec.adapter.input.web.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.adapter.input.web.news.dto.NewsRequest;
import ru.clevertec.adapter.input.web.news.dto.NewsResponse;
import ru.clevertec.adapter.input.web.news.dto.NewsUpdateRequest;
import ru.clevertec.client.comment.CommentClient;
import ru.clevertec.config.ApplicationNoSecurity;
import ru.clevertec.util.TestDataIT;

import java.security.Principal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 2411)
@Import(ApplicationNoSecurity.class)
@Sql(scripts = "classpath:db/data.sql")
class NewsControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private static final String RESPONSE_BODY = "userResponse#ok.json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON_VALUE = "application/json";

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CommentClient commentClient;

    @Test
    void shouldReadAllNews() throws Exception {
        //given
        int pageNumber = 0;
        int pageSize = 3;
        NewsResponse newsResponse = TestDataIT.generateNewsResponse();

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/news")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize));

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.length()").value(3),
                        jsonPath("$[0].id").value(newsResponse.id().toString()),
                        jsonPath("$[0].title").value(newsResponse.title()),
                        jsonPath("$[0].text").value(newsResponse.text()),
                        jsonPath("$[0].authorId").value(newsResponse.authorId().toString())
                );
    }

    @Test
    void shouldReadNewsById_whenNewsExist() throws Exception {
        //given
        UUID newsId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        NewsResponse newsResponse = TestDataIT.generateNewsResponse();

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/news/{id}", newsId);

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(newsId.toString()),
                        jsonPath("$.title").value(newsResponse.title()),
                        jsonPath("$.text").value(newsResponse.text()),
                        jsonPath("$.authorId").value(newsResponse.authorId().toString())
                );
    }

    @Test
    void shouldNotReadNewsById_whenNewsNotExist() throws Exception {
        //given
        UUID wrongId = UUID.fromString("11111111-1111-1111-1111-111111111112");

        MockHttpServletRequestBuilder requestBuilder = get("/api/v1/news/{id}", wrongId);

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNews() throws Exception {
        //given
        NewsRequest newsRequest = TestDataIT.generateNewsRequest();
        NewsResponse newsResponse = TestDataIT.generateNewsResponse();
        String jsonNewsRequest = objectMapper.writeValueAsString(newsRequest);
        UUID authorId = newsResponse.authorId();

        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName())
                .thenReturn(authorId.toString());

        WireMock.stubFor(WireMock.get("/api/v1/users/" + authorId)
                .willReturn(WireMock.aResponse()
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBodyFile(RESPONSE_BODY)));


        MockHttpServletRequestBuilder requestBuilder = post("/api/v1/news")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .content(jsonNewsRequest)
                .with(request -> {
                    request.setUserPrincipal(mockPrincipal);
                    return request;
                });

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.title").value(newsResponse.title()),
                        jsonPath("$.text").value(newsResponse.text()),
                        jsonPath("$.authorId").value(newsResponse.authorId().toString())
                );
    }

    @Test
    void shouldUpdateNews() throws Exception {
        //given
        NewsUpdateRequest newsUpdateRequest = TestDataIT.generateNewsUpdateRequest();
        NewsResponse newsResponse = TestDataIT.generateNewsResponse();
        String jsonNewsRequest = objectMapper.writeValueAsString(newsUpdateRequest);

        MockHttpServletRequestBuilder requestBuilder = put("/api/v1/news")
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .content(jsonNewsRequest);

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpectAll(status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").isNotEmpty(),
                        jsonPath("$.title").value(newsResponse.title()),
                        jsonPath("$.text").value(newsResponse.text()),
                        jsonPath("$.authorId").value(newsResponse.authorId().toString())
                );
    }

    @Test
    void shouldDeleteById() throws Exception {
        //given
        UUID newsId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        doNothing()
                .when(commentClient).deleteAllComments(any());

        MockHttpServletRequestBuilder requestBuilder = delete("/api/v1/news/" + newsId);

        //when, then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }
}