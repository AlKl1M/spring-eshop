package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.controller.payload.ProductReviewDto;
import com.bfu.feedbackservice.entity.ProductReview;
import com.bfu.feedbackservice.repository.ProductReviewRepostiory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewsServiceImpl implements ProductReviewsService {
    private final ProductReviewRepostiory productReviewRepostiory;
    @Override
    public ProductReview createProductReview(String productId, int rating, String review, String username, String userId) {
        Optional<ProductReview> existingReview = productReviewRepostiory.findByProductIdAndUserId(productId, userId);
        if (existingReview.isPresent()) {
            throw new IllegalStateException("User already has a review for this product");
        }
        return productReviewRepostiory.save(new ProductReview(UUID.randomUUID(), productId, rating, review, new Date(), username, userId));
    }

    @Override
    public List<ProductReviewDto> findProductReviewsByProduct(String productId) {
        List<ProductReview> productReviews = productReviewRepostiory.findAllByProductId(productId);
        return productReviews.stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductReviewDto> findLatestProductReviews() {
        List<ProductReview> latestProductReviews = productReviewRepostiory.findTop10ByOrderByCreatedDateDesc();
        return latestProductReviews.stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProductReview(String productId, int rating, String review, String userId) {
        Optional<ProductReview> optionalProductReview = productReviewRepostiory.findByProductIdAndUserId(productId, userId);
        if (optionalProductReview.isPresent()) {
            ProductReview oldProductReview = optionalProductReview.get();
            oldProductReview.setProductId(productId);
            oldProductReview.setRating(rating);
            oldProductReview.setReview(review);
            oldProductReview.setCreatedDate(new Date());
            oldProductReview.setUsername(oldProductReview.getUsername());
            productReviewRepostiory.save(oldProductReview);
        }
    }

    @Override
    public void deleteProductReview(String productId, String userId) {
        productReviewRepostiory.deleteByProductIdAndUserId(productId, userId);
    }
}
