package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.entity.FavouriteProduct;

import java.util.List;

public interface FavouriteProductsService {
    FavouriteProduct addProductToFavourites(String productId, String userId);
    void removeProductFromFavourites(String productId, String userId);
    List<FavouriteProduct> findFavouriteProductByProduct(String productId, String userId);
    List<FavouriteProduct> findFavouriteProducts(String userId);
    List<String> findProductIdsByUserId(String userId);
}
