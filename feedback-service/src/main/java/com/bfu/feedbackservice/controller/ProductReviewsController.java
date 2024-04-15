package com.bfu.feedbackservice.controller;

import com.bfu.feedbackservice.controller.payload.NewProductReviewPayload;
import com.bfu.feedbackservice.entity.ProductReview;
import com.bfu.feedbackservice.service.ProductReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/product-reviews")
@RequiredArgsConstructor
public class ProductReviewsController {
    private final ProductReviewsService productReviewsService;

    @GetMapping("by-product-id/{productId}")
    public List<ProductReview> findProductReviewsByProductId(@PathVariable("productId") String productId) {
        return productReviewsService.findProductReviewsByProduct(productId);
    }

    @PostMapping
    public ProductReview createProductReview(
            @RequestBody NewProductReviewPayload payload,
            Principal principal
    ) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        return productReviewsService.createProductReview(payload.productId(), payload.rating(), payload.review(), userId);
    }
}
