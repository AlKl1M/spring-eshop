package com.bfu.feedbackservice.controller;

import com.bfu.feedbackservice.entity.FavouriteProduct;
import com.bfu.feedbackservice.entity.ProductReview;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 54321)
class ProductReviewsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        this.mongoTemplate.insertAll(List.of(
                new ProductReview(UUID.randomUUID(), "1", 5, "I love this product!", new Date(1632144000000L), "John", "user"),
                new ProductReview(UUID.randomUUID(), "2", 4, "Good product", new Date(1632144000000L), "John", "user"),
                new ProductReview(UUID.randomUUID(), "3", 1, "Bad product!", new Date(1632144000000L), "John", "user")
        ));
    }

    @AfterEach
    void tearDown() {
        this.mongoTemplate.remove(FavouriteProduct.class).all();
    }

    @Test
    void findProductReviewsByProductId_withValidPayload_ReturnsProductReviewsByProduct() throws Exception {
        this.mongoTemplate.insertAll(List.of(
                new ProductReview(UUID.fromString("f3f107ff-6935-4706-ab94-73d6c2a2004c"), "4", 5, "I love this product!", new Date(1632144000000L), "John", "user"),
                new ProductReview(UUID.fromString("c839eada-497b-4226-b1dd-dcbf25627cd4"), "5", 4, "Good product", new Date(1632144000000L), "John", "user"),
                new ProductReview(UUID.fromString("edc1403e-1fb1-4402-814e-584ad4dc5ad5"), "6", 1, "Bad product!", new Date(1632144000000L), "John", "user")
        ));

        var requestBuilder = MockMvcRequestBuilders.get("/api/product-reviews/by-product-id/{productId}", "4")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                         [{"id":"f3f107ff-6935-4706-ab94-73d6c2a2004c","productId":"4","rating":5,"review":"I love this product!","currectDate":"2021-09-20T13:20:00.000+00:00","username":"John"}]
                        """)
                );
    }

    @Test
    void findTop10Reviews_withValidPayload_ReturnsLastReviews() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/product-reviews/top10")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                        [
                        {"id":"92ee42c2-e824-4cca-808d-9c5d8c695115","productId":"1","rating":5,"review":"I love this product!","currectDate":"2021-09-20T13:20:00.000+00:00","username":"John"},
                        {"id":"6290cbc8-aa03-4d9a-acce-b6207ed1d57b","productId":"2","rating":4,"review":"Good product","currectDate":"2021-09-20T13:20:00.000+00:00","username":"John"},
                        {"id":"a8ef1517-a983-4f09-a2da-97f53e367455","productId":"3","rating":1,"review":"Bad product!","currectDate":"2021-09-20T13:20:00.000+00:00","username":"John"}
                        ]
                        """)
                );
    }

    @Test
    void createProductReview_WithValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "productId": "5",
                    "rating": 2,
                    "review": "Kinda bad"
                    }
                """)
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/catalogue/is-exists"))
                .withQueryParam("productId", WireMock.equalTo("5"))
                .willReturn(WireMock.ok("""                   
                        true
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void createProductReview_WithInvalidPayload_ReturnsErrors() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "productId": null,
                    "rating": null,
                    "review": null
                    }
                """)
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/catalogue/is-exists"))
                .withQueryParam("productId", WireMock.equalTo("5"))
                .willReturn(WireMock.ok("""                   
                        true
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                                {"errors": ["Product id must be specified","Product rating must be specified"]}
                        """)
                );
    }

    @Test
    void updateProductReview_WithValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.put("/api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "productId": "4",
                    "rating": 2,
                    "review": "Kinda bad"
                    }
                """)
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    void updateProductReview_WithInvalidPayload_ReturnsErrors() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.put("/api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "productId": null,
                    "rating": null,
                    "review": null
                    }
                """)
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().json("""
                        {"errors":["Product rating must be specified","Product id must be specified"]}
                        """)
                );
    }

    @Test
    void deleteProductReview_withValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/api/product-reviews/{productId}", "1")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                );
    }

}