package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.entity.FavouriteProduct;
import com.bfu.feedbackservice.repository.FavouriteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteProductsServiceImpl implements FavouriteProductsService {
    private final FavouriteProductRepository favouriteProductRepository;
    @Override
    public FavouriteProduct addProductToFavourites(String productId, String userId) {
        List<FavouriteProduct> existingFavourites = favouriteProductRepository.findByProductIdAndUserId(productId, userId);
        if (existingFavourites.isEmpty()) {
            return favouriteProductRepository.save(new FavouriteProduct(UUID.randomUUID(), productId, userId));
        } else {
            throw new IllegalStateException("Product already exists in favorites.");
        }
    }

    @Override
    public void removeProductFromFavourites(String productId, String userId) {
        this.favouriteProductRepository.deleteByProductIdAndUserId(productId, userId);
    }

    @Override
    public List<FavouriteProduct> findFavouriteProductByProduct(String productId, String userId) {
        return this.favouriteProductRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public List<FavouriteProduct> findFavouriteProducts(String userId) {
        return this.favouriteProductRepository.findAllByUserId(userId);
    }
}
