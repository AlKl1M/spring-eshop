package com.bfu.cartservice.service;

import com.bfu.cartservice.controller.payload.ArrayOfSimplifiedProduct;
import com.bfu.cartservice.entity.Cart;
import com.bfu.cartservice.entity.Product;
import com.bfu.cartservice.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    CartRepository cartRepository;
    @InjectMocks
    CartServiceImpl cartService;

    @Test
    public void getCart_WithExistingCart_ReturnsExistingProducts() {
        Cart cart = new Cart(UUID.randomUUID(), "testUserId", Arrays.asList(
                new Product("1", "Product 1", new BigDecimal("10.0"), 2),
                new Product("2", "Product 2", new BigDecimal("20.0"), 1)
        ), new BigDecimal("30.0"));

        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.of(cart));

        CartServiceImpl cartService = new CartServiceImpl(cartRepository);

        ArrayOfSimplifiedProduct arrayOfSimplifiedProduct = cartService.getCartByUserId("testUserId");

        assertEquals(2, arrayOfSimplifiedProduct.products().size());
        assertEquals("1", arrayOfSimplifiedProduct.products().get(0).productId());
        assertEquals("Product 1", arrayOfSimplifiedProduct.products().get(0).name());
        assertEquals(2, arrayOfSimplifiedProduct.products().get(0).quantity());
        assertEquals(new BigDecimal("10.0"), arrayOfSimplifiedProduct.products().get(0).price());

        assertEquals("2", arrayOfSimplifiedProduct.products().get(1).productId());
        assertEquals("Product 2", arrayOfSimplifiedProduct.products().get(1).name());
        assertEquals(1, arrayOfSimplifiedProduct.products().get(1).quantity());
        assertEquals(new BigDecimal("20.0"), arrayOfSimplifiedProduct.products().get(1).price());
    }

    @Test
    public void getCart_WithNonExistingCart_ReturnsEmptyCart() {
        when(cartRepository.findByUserId("testUserId")).thenReturn(Optional.empty());

        CartServiceImpl cartService = new CartServiceImpl(cartRepository);

        ArrayOfSimplifiedProduct arrayOfSimplifiedProduct = cartService.getCartByUserId("testUserId");

        assertEquals(0, arrayOfSimplifiedProduct.products().size());
    }

    @Test
    public void createCart_WithValidPayload_ReturnsNewCart() {
        String userId = "testUserId";
        Cart createdCart = new Cart(UUID.randomUUID(), userId, Collections.emptyList(), BigDecimal.ZERO);

        when(cartRepository.save(any(Cart.class))).thenReturn(createdCart);

        Cart result = cartService.createCartByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void addToCart_WithValidPayload_ReturnsNewProductInCart() {
        String userId = "testUserId";
        Product newProduct = new Product("1", "Test Product", BigDecimal.TEN, 1);
        Cart existingCart = new Cart(UUID.randomUUID(), userId, new ArrayList<>(), BigDecimal.ZERO);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(existingCart);

        cartService.addToCart(userId, newProduct);

        assertEquals(1, existingCart.getProducts().size());
        assertEquals(BigDecimal.TEN, existingCart.getTotalPrice());
        verify(cartRepository, times(1)).findByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void increaseProductQuantity_withValidPayload_ReturnsIncreasedQuantity() {
        String userId = "123";
        String productId = "456";
        BigDecimal price = new BigDecimal("10.00");

        Cart cart = new Cart();
        cart.setUserId(userId);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("20.00"));
        product.setQuantity(1);
        cart.setProducts(Collections.singletonList(product));
        cart.setTotalPrice(new BigDecimal("20.00"));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.increaseProductQuantity(userId, productId, price);

        assertEquals(2, cart.getProducts().get(0).getQuantity());
        assertEquals(new BigDecimal("30.00"), cart.getProducts().get(0).getPrice());
        assertEquals(new BigDecimal("30.00"), cart.getTotalPrice());
    }

    @Test
    public void addToCart_withExistingProduct_ReturnsNewProductQuantity() {
        String userId = "testUser";
        Product existingProduct = new Product("1", "Existing Product", BigDecimal.TEN, 1);
        Product newProduct = new Product("1", "Existing Product", BigDecimal.TEN, 2);
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>(), BigDecimal.ZERO);
        cart.getProducts().add(existingProduct);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.addToCart(userId, newProduct);

        assertEquals(3, cart.getProducts().get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(30), cart.getProducts().get(0).getPrice());
    }

    @Test
    public void reduceProductQuantity_withValidPayload_ReturnsReducedQuantityOfProducts() {
        String userId = "testUserId";
        String productId = "testProductId";
        BigDecimal price = new BigDecimal("10.00");

        Cart cart = new Cart();
        cart.setUserId(userId);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("20.00"));
        product.setQuantity(2);
        cart.setProducts(Collections.singletonList(product));
        cart.setTotalPrice(new BigDecimal("40.00"));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.reduceProductQuantity(userId, productId, price);

        assertEquals(1, cart.getProducts().size());
        assertEquals(1, cart.getProducts().get(0).getQuantity());
        assertEquals(new BigDecimal("10.00"), cart.getProducts().get(0).getPrice());
        assertEquals(new BigDecimal("30.00"), cart.getTotalPrice());
    }

    @Test
    public void reduceProductQuantity_withNotExistingProduct_ThrowsException() {
        String userId = "testUserId";
        String productId = "testProductId";
        BigDecimal price = new BigDecimal("10.00");

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(new Cart()));

        assertThrows(RuntimeException.class, () -> cartService.reduceProductQuantity(userId, productId, price));
    }

    @Test
    public void deleteAllProducts_withValidPayload_ReturnsEmptyCart() {
        String userId = "testUserId";

        Cart cart = new Cart();
        cart.setUserId(userId);
        Product product = new Product();
        product.setId("testProductId");
        product.setPrice(new BigDecimal("20.00"));
        product.setQuantity(2);
        cart.setProducts(Collections.singletonList(product));
        cart.setTotalPrice(new BigDecimal("40.00"));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.deleteAllProducts(userId);

        assertTrue(cart.getProducts().isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
    }

    @Test
    void deleteProductFromCart_withValidProduct_returnsNoProductInCart() {
        String userId = "testUserId";
        String productId = "testProductId";

        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>(), BigDecimal.ZERO);
        Product product = new Product(productId, "Product 1", new BigDecimal("10.00"), 1);
        cart.getProducts().add(product);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.deleteProductFromCart(userId, productId);

        assertEquals(0, cart.getProducts().size());
        assertEquals(BigDecimal.ZERO, cart.getTotalPrice());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void deleteProductFromCart_WithNotExistingUser_ReturnsNothing() {
        String userId = "testUserId";
        String productId = "testProductId";

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        cartService.deleteProductFromCart(userId, productId);

        verify(cartRepository, never()).save(any());
    }

}