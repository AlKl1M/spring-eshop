package com.bfu.feedbackservice.service;

import com.bfu.feedbackservice.controller.payload.ProductReviewDto;
import com.bfu.feedbackservice.entity.ProductReview;
import com.bfu.feedbackservice.repository.ProductReviewRepostiory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductReviewsServiceImplTest {
    @Mock
    ProductReviewRepostiory productReviewRepostiory;
    @InjectMocks
    ProductReviewsServiceImpl productReviewsService;

    @Test
    public void createProductReview_WithValidPayload_ReturnsCreatedReview() {
        when(productReviewRepostiory.findByProductIdAndUserId("123", "user123")).thenReturn(Optional.empty());
        when(productReviewRepostiory.save(any(ProductReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductReview createdReview = productReviewsService.createProductReview("123", 5, "Great product!", "john_doe", "user123");

        verify(productReviewRepostiory, times(1)).save(any(ProductReview.class));

        assertNotNull(createdReview);
        assertEquals("123", createdReview.getProductId());
        assertEquals(5, createdReview.getRating());
        assertEquals("Great product!", createdReview.getReview());
        assertEquals("john_doe", createdReview.getUsername());
        assertEquals("user123", createdReview.getUserId());
    }

    @Test
    public void createProductReview_withExistingReview_ReturnsIllegalStateException() {
        String productId = "123";
        int rating = 5;
        String review = "Great product!";
        String username = "john_doe";
        String userId = "user123";

        when(productReviewRepostiory.findByProductIdAndUserId(productId, userId)).thenReturn(Optional.of(new ProductReview()));

        assertThrows(IllegalStateException.class, () -> productReviewsService.createProductReview(productId, rating, review, username, userId));
    }

    @Test
    public void findProductReviewsByProduct_withValidPayload_ReturnsUserReviews() {
        String productId = "123";
        List<ProductReview> productReviews = Arrays.asList(
                new ProductReview(UUID.randomUUID(), productId, 5, "Great product", new Date(), "user1", "userId1"),
                new ProductReview(UUID.randomUUID(), productId, 4, "Good product", new Date(), "user2", "userId2")
        );

        when(productReviewRepostiory.findAllByProductId(productId)).thenReturn(productReviews);

        List<ProductReviewDto> result = productReviewsService.findProductReviewsByProduct(productId);

        assertEquals(2, result.size());
    }

    @Test
    public void findLatestProductReviews_withMoreThan10Rewiews_Returns10Reviews() {
        List<ProductReview> mockReviews = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            ProductReview review = new ProductReview(UUID.randomUUID(), "productId" + i, i, "Review " + i, new Date(), "User" + i, "userId" + i);
            mockReviews.add(review);
        }

        when(productReviewRepostiory.findTop10ByOrderByCreatedDateDesc()).thenReturn(mockReviews);

        List<ProductReviewDto> result = productReviewsService.findLatestProductReviews();

        assertEquals(mockReviews.stream()
                .map(ProductReviewDto::from)
                .collect(Collectors.toList()), result);
    }

    @Test
    public void updateProductReview_withValidPayload_returnsUpdatedReview() {
        String productId = "123";
        int rating = 4;
        String review = "Updated review";
        String userId = "userId1";

        ProductReview existingReview = new ProductReview(UUID.randomUUID(), productId, 3, "Old review", new Date(), "user1", userId);
        when(productReviewRepostiory.findByProductIdAndUserId(productId, userId)).thenReturn(Optional.of(existingReview));

        productReviewsService.updateProductReview(productId, rating, review, userId);

        verify(productReviewRepostiory).save(existingReview);
        assertEquals(rating, existingReview.getRating());
        assertEquals(review, existingReview.getReview());
    }

    @Test
    public void deleteProductReview_withValidPayload_removesProductReview() {
        String productId = "123";
        String userId = "userId1";

        productReviewsService.deleteProductReview(productId, userId);

        verify(productReviewRepostiory).deleteByProductIdAndUserId(productId, userId);
    }
}