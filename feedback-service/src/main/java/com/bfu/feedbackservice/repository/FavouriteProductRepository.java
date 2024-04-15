package com.bfu.feedbackservice.repository;

import com.bfu.feedbackservice.entity.FavouriteProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface FavouriteProductRepository extends MongoRepository<FavouriteProduct, UUID> {
    List<FavouriteProduct> findAllByUserId(String userId);
    void deleteByProductIdAndUserId(String productId, String userId);
    List<FavouriteProduct> findByProductIdAndUserId(String productId, String userId);
}
