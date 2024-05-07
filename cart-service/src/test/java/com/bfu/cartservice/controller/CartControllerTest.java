package com.bfu.cartservice.controller;

import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WireMockTest(httpPort = 54321)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        this.mongoTemplate.insertAll(List.of(
                new Cart(UUID.randomUUID(), "user", Arrays.asList(
                        new Product("1", "Product 1", new BigDecimal("10.0"), "photo1", 2),
                        new Product("2", "Product 2", new BigDecimal("20.0"), "photo2", 1)
                ), new BigDecimal("30.0"))));
    }

    @AfterEach
    void tearDown() {
        this.mongoTemplate.remove(Cart.class).all();
    }

    @Test
    void getUserCart_WithValidPayload_ReturnsCartProducts() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/carts/cart")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().json("""
                        {
                        "products": [
                        {
                        "productId": "1",
                        "name": "Product 1",
                        "quantity": 2,
                        "price": 10.0,
                        "preview": "photo1"
                        },
                        {
                        "productId": "2",
                        "name": "Product 2",
                        "quantity": 1,
                        "price": 20.0,
                        "preview": "photo2"
                        }
                        ]}
                        """)
                );
    }

    @Test
    void addToCart_withValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/api/carts/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "productId": "b7f39ec3-c11c-4",
                    "quantity": 1
                    }
                """)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/catalogue/simplified-product-info"))
                .withQueryParam("productId", WireMock.equalTo("b7f39ec3-c11c-4"))
                .willReturn(WireMock.ok("""
                        {
                        "productId": "b7f39ec3-c11c-4",
                         "name": "Addded",
                         "price": 10.00,
                         "preview": "photob7f39ec3-c11c-4"
                         }
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Product added to cart"))
                );
    }

    @Test
    void addToCart_withInvalidPayload_ReturnsBadRequestResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/api/carts/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "productId": null,
                    "quantity": -1
                    }
                """)
                .locale(Locale.ENGLISH)
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/catalogue/simplified-product-info"))
                .withQueryParam("productId", WireMock.equalTo("b7f39ec3-c11c-4"))
                .willReturn(WireMock.ok("""
                        {
                        "productId": "b7f39ec3-c11c-4",
                         "name": "Addded",
                         "price": 10.00,
                         "preview": "photob7f39ec3-c11c-4"
                         }
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        content().json("""
                        {
                        "errors": [
                            "Product id is null",
                            "Quantity below 1"
                        ]
                        }
                        """));
    }

    @Test
    void increaseCart_WithValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.patch("/api/carts/cart/increase")
                .queryParam("productId", "b7f39ec3-c11c-4")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/catalogue/simplified-product-info"))
                .withQueryParam("productId", WireMock.equalTo("b7f39ec3-c11c-4"))
                .willReturn(WireMock.ok("""
                        {
                        "productId": "b7f39ec3-c11c-4",
                        "name": "Addded",
                        "price": 10.00,
                        "preview": "photob7f39ec3-c11c-4"
                        }
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Product quantity increased"))
                );
    }

    @Test
    void reduceCart_withValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.patch("/api/carts/cart/reduce")
                .queryParam("productId", "b7f39ec3-c11c-4")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/catalogue/simplified-product-info"))
                .withQueryParam("productId", WireMock.equalTo("b7f39ec3-c11c-4"))
                .willReturn(WireMock.ok("""
                        {
                        "productId": "b7f39ec3-c11c-4",
                         "name": "Addded",
                         "price": 10.00,
                         "preview": "photob7f39ec3-c11c-4"}
                        """).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Product quantity reduced"))
                );
    }

    @Test
    void deleteCart_withValidPayload_returnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/api/carts/cart")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Cart cleared"))
                );
    }

    @Test
    void deleteProductFromCart_withValidPayload_ReturnsValidResponse() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.delete("/api/carts/cart", "b7f39ec3-c11c-4")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Cart cleared"))
                );
    }
}