package com.bfu.cartservice.repository;

import com.bfu.cartservice.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends MongoRepository<Cart, UUID> {
    Optional<Cart> findByUserId(String userId);
    List<Cart> findAllByUserId(String userId);
}
