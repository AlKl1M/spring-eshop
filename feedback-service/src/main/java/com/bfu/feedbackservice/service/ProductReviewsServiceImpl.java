package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.entity.ProductReview;
import com.bfu.feedbackservice.repository.ProductReviewRepostiory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReviewsServiceImpl implements ProductReviewsService {
    private final ProductReviewRepostiory productReviewRepostiory;
    @Override
    public ProductReview createProductReview(String productId, int rating, String review, String userId) {
        Optional<ProductReview> existingReview = productReviewRepostiory.findByProductIdAndUserId(productId, userId);
        if (existingReview.isPresent()) {
            throw new IllegalStateException("User already has a review for this product");
        }
        return productReviewRepostiory.save(new ProductReview(UUID.randomUUID(), productId, rating, review, userId));
    }

    @Override
    public List<ProductReview> findProductReviewsByProduct(String productId) {
        return productReviewRepostiory.findAllByProductId(productId);
    }

    @Override
    public void updateProductReview(String productId, int rating, String review, String userId) {
        Optional<ProductReview> optionalProductReview = productReviewRepostiory.findByProductIdAndUserId(productId, userId);
        if (optionalProductReview.isPresent()) {
            ProductReview oldProductReview = optionalProductReview.get();
            oldProductReview.setRating(rating);
            oldProductReview.setReview(review);
            productReviewRepostiory.save(oldProductReview);
        }
    }

    @Override
    public void deleteProductReview(String productId, String userId) {
        productReviewRepostiory.deleteByProductIdAndUserId(productId, userId);
    }
}
