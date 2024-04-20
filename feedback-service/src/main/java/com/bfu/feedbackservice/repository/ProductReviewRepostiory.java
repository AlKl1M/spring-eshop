package com.bfu.feedbackservice.repository;

import com.bfu.feedbackservice.entity.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductReviewRepostiory extends MongoRepository<ProductReview, UUID> {
    List<ProductReview> findAllByProductId(String productId);
    Optional<ProductReview> findByProductIdAndUserId(String productId, String userId);
    void deleteByProductIdAndUserId(String productId, String userId);
}
