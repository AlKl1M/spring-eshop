package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.entity.FavouriteProduct;
import com.bfu.feedbackservice.repository.FavouriteProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavouriteProductsServiceImplTest {
    @Mock
    FavouriteProductRepository favouriteProductRepository;
    @InjectMocks
    FavouriteProductsServiceImpl favouriteProductsService;

    @Test
    public void testAddProductToFavourites_Success() {
        String productId = "123";
        String userId = "456";
        FavouriteProduct newFavouriteProduct = new FavouriteProduct(UUID.randomUUID(), productId, userId);
        when(favouriteProductRepository.findByProductIdAndUserId(productId, userId)).thenReturn(new ArrayList<>());
        when(favouriteProductRepository.save(ArgumentMatchers.any(FavouriteProduct.class))).thenReturn(newFavouriteProduct);

        FavouriteProduct result = favouriteProductsService.addProductToFavourites(productId, userId);

        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void testAddProductToFavourites_Failure() {
        String productId = "123";
        String userId = "456";
        List<FavouriteProduct> existingFavourites = new ArrayList<>();
        existingFavourites.add(new FavouriteProduct(UUID.randomUUID(), productId, userId));
        when(favouriteProductRepository.findByProductIdAndUserId(productId, userId)).thenReturn(existingFavourites);

        assertThrows(IllegalStateException.class, () -> {
            favouriteProductsService.addProductToFavourites(productId, userId);
        });
    }

    @Test
    public void testRemoveProductFromFavourites() {
        // Arrange
        String productId = "123";
        String userId = "456";

        // Act
        favouriteProductsService.removeProductFromFavourites(productId, userId);

        // Assert
        verify(favouriteProductRepository, times(1)).deleteByProductIdAndUserId(productId, userId);
    }

    @Test
    public void testFindFavouriteProductByProduct() {
        // Arrange
        String productId = "123";
        String userId = "456";
        List<FavouriteProduct> expectedProducts = new ArrayList<>();
        when(favouriteProductRepository.findByProductIdAndUserId(productId, userId)).thenReturn(expectedProducts);

        // Act
        List<FavouriteProduct> result = favouriteProductsService.findFavouriteProductByProduct(productId, userId);

        // Assert
        assertSame(expectedProducts, result);
    }

    @Test
    public void testFindFavouriteProducts() {
        // Arrange
        String userId = "456";
        List<FavouriteProduct> expectedProducts = new ArrayList<>();
        when(favouriteProductRepository.findAllByUserId(userId)).thenReturn(expectedProducts);

        // Act
        List<FavouriteProduct> result = favouriteProductsService.findFavouriteProducts(userId);

        // Assert
        assertSame(expectedProducts, result);
    }

    @Test
    public void testFindProductIdsByUserId() {
        // Arrange
        String userId = "456";
        List<FavouriteProduct> favouriteProducts = new ArrayList<>();
        favouriteProducts.add(new FavouriteProduct(UUID.randomUUID(), "123", userId));
        favouriteProducts.add(new FavouriteProduct(UUID.randomUUID(), "456", userId));
        when(favouriteProductRepository.findAllByUserId(userId)).thenReturn(favouriteProducts);

        // Act
        List<String> result = favouriteProductsService.findProductIdsByUserId(userId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains("123"));
        assertTrue(result.contains("456"));
    }
}