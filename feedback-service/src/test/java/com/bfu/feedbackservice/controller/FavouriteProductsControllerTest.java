package com.bfu.feedbackservice.controller;

import com.bfu.feedbackservice.entity.FavouriteProduct;
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
class FavouriteProductsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        this.mongoTemplate.insertAll(List.of(
                new FavouriteProduct(UUID.fromString("f3f107ff-6935-4706-ab94-73d6c2a2004c"), "1", "user"),
                new FavouriteProduct(UUID.fromString("c839eada-497b-4226-b1dd-dcbf25627cd4"), "2", "user"),
                new FavouriteProduct(UUID.fromString("edc1403e-1fb1-4402-814e-584ad4dc5ad5"), "3", "user")
        ));
    }

    @AfterEach
    void tearDown() {
        this.mongoTemplate.remove(FavouriteProduct.class).all();
    }

    @Test
    void getFavouriteProducts_WithValidPayload_ReturnsUserFavouriteProducts() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/favourite-products")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                        [
                        {"id":"f3f107ff-6935-4706-ab94-73d6c2a2004c","productId":"1","userId":"user"},
                        {"id":"c839eada-497b-4226-b1dd-dcbf25627cd4","productId":"2","userId":"user"},
                        {"id":"edc1403e-1fb1-4402-814e-584ad4dc5ad5","productId":"3","userId":"user"}
                        ]
                        """)
                );
    }

    @Test
    void findProductsIdByUserId_WithValidPayload_ReturnsProductsId() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/favourite-products/productsId")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                        {"productsId":["1","2","3"]}
                        """)
                );
    }

    @Test
    void findFavouriteProductByProductId_WithValidPayload_ReturnsFavouriteProduct() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/favourite-products/by-product-id/{productId}", "1")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                        [{"id":"f3f107ff-6935-4706-ab94-73d6c2a2004c","productId":"1","userId":"user"}]
                        """)
                );
    }

    @Test
    void addProductToFavourites_withValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/api/favourite-products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "productId": "4"
                    }
                """)
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/catalogue/is-exists"))
                .withQueryParam("productId", WireMock.equalTo("4"))
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
    void removeProductFromFavourites_withValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/api/favourite-products/by-product-id/{productId}", 1)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk()
                );
    }
}