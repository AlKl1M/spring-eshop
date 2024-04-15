package com.bfu.feedbackservice.controller;

import com.bfu.feedbackservice.controller.payload.NewFavouriteProductPayload;
import com.bfu.feedbackservice.entity.FavouriteProduct;
import com.bfu.feedbackservice.service.FavouriteProductsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/favourite-products")
@RequiredArgsConstructor
public class FavouriteProductsController {
    private final FavouriteProductsService favouriteProductsService;

    @GetMapping
    public List<FavouriteProduct> findFavouriteProducts(Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        return favouriteProductsService.findFavouriteProducts(userId);
    }

    @GetMapping("by-product-id/{productId}")
    public List<FavouriteProduct> findFavouriteProductByProductId(@PathVariable("productId") String productId,
                                                                  Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        return this.favouriteProductsService.findFavouriteProductByProduct(productId, userId);
    }

    @PostMapping
    public FavouriteProduct addProductToFavourites(
            @Valid @RequestBody NewFavouriteProductPayload payload,
            BindingResult bindingResult,
            Principal principal) throws BindException{
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
            return favouriteProductsService.addProductToFavourites(payload.productId(), userId);
        }
    }

    @DeleteMapping("by-product-id/{productId}")
    public void removeProductFromFavourites(@PathVariable("productId") String productId,
                                                         Principal principal) {
        String userId = ((JwtAuthenticationToken) principal).getToken().getSubject();
        favouriteProductsService.removeProductFromFavourites(productId, userId);
    }
}
