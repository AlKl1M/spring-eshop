package com.bfu.feedbackservice.controller;

import com.bfu.feedbackservice.client.CatalogueClient;
import com.bfu.feedbackservice.controller.payload.NewProductReviewPayload;
import com.bfu.feedbackservice.controller.payload.ProductReviewDto;
import com.bfu.feedbackservice.controller.payload.UpdateProductReviewPayload;
import com.bfu.feedbackservice.exception.ProductNotFoundException;
import com.bfu.feedbackservice.service.ProductReviewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/product-reviews")
@RequiredArgsConstructor
public class ProductReviewsController {
    private final ProductReviewsService productReviewsService;
    private final CatalogueClient client;

    @GetMapping("by-product-id/{productId}")
    public List<ProductReviewDto> findProductReviewsByProductId(@PathVariable("productId") String productId) {
        return productReviewsService.findProductReviewsByProduct(productId);
    }

    @GetMapping("top10")
    public List<ProductReviewDto> findTop10Reviews() {
        return productReviewsService.findLatestProductReviews();
    }

    @PostMapping
    public ResponseEntity<?> createProductReview(
            @Valid @RequestBody NewProductReviewPayload payload,
            Principal principal) {
        if (client.isProductExist(payload.productId())) {
            String userName = ((JwtAuthenticationToken) principal).getToken().getClaimAsString("preferred_username");
            String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
            productReviewsService.createProductReview(payload.productId(), payload.rating(), payload.review(), userName, userId);
            return ResponseEntity.ok("Product review created successfully");
        } else {
            throw new ProductNotFoundException("Product does not exist");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateProductReview(@Valid @RequestBody UpdateProductReviewPayload payload,
                                                 Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        productReviewsService.updateProductReview(payload.productId(),payload.rating(), payload.review(), userId);
        return ResponseEntity.ok("Product rewiew updated successfully");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProductReview(@PathVariable String productId,
                                                 Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        productReviewsService.deleteProductReview(productId, userId);
        return ResponseEntity.ok("Product rewiew updated successfully");
    }
}
