package com.bfu.catalogueservice.service.product;

import com.bfu.catalogueservice.controller.payload.Category.CategoryResponse;
import com.bfu.catalogueservice.controller.payload.Product.CreateProductRequest;
import com.bfu.catalogueservice.controller.payload.Product.DeleteProductRequest;
import com.bfu.catalogueservice.controller.payload.Product.ProductResponse;
import com.bfu.catalogueservice.controller.payload.Product.UpdateProductRequest;
import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.JsonNodeConverter;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.exception.BrandNotFoundException;
import com.bfu.catalogueservice.exception.CategoryNotFoundException;
import com.bfu.catalogueservice.exception.ProductNotFoundException;
import com.bfu.catalogueservice.repository.BrandRepository;
import com.bfu.catalogueservice.repository.CategoryRepository;
import com.bfu.catalogueservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    @Override
    public void createProduct(CreateProductRequest productRequest) {
        Brand brand = brandRepository.findByBrandId(productRequest.brandId());
        Category category = categoryRepository.findByCategoryId(productRequest.categoryId());
        if (brand != null) {
            if (category != null) {
                Product product = Product.builder()
                        .productId(UUID.randomUUID().toString().substring(0,15))
                        .price(productRequest.price())
                        .name(productRequest.name())
//                        .attributes(productRequest.attributes())
                        .brand(brand)
                        .category(category)
                        .build();
                productRepository.save(product);
            }
            else {
                throw new CategoryNotFoundException(productRequest.categoryId());
            }
        }
        else {
            throw new BrandNotFoundException(productRequest.brandId());
        }
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> products = new ArrayList<>();
        for (Product product: productRepository.findAll()){
            products.add(ProductResponse.from(product));
        }
        return products;
    }

    @Override
    public void updateProduct(UpdateProductRequest productRequest) {
        Brand brand = brandRepository.findByBrandId(productRequest.newBrandId());
        Category category = categoryRepository.findByCategoryId(productRequest.newCategoryId());
        Product product = productRepository.findByProductId(productRequest.productId());
        if (brand != null) {
            product.setBrand(brand);
        }
        if (category != null) {
            product.setCategory(category);
        }
        if (productRequest.newPrice() != null) {
            product.setPrice(productRequest.newPrice());
        }
        if (productRequest.newName() != null) {
            product.setName(productRequest.newName());
        }
        productRepository.save(product);

    }

    @Override
    public void deleteProduct(DeleteProductRequest productRequest) {
        Product product = productRepository.findByProductId(productRequest.productId());
        if (product != null) {
            productRepository.delete(product);
        }
        else {
            throw new ProductNotFoundException(productRequest.productId());
        }
    }

}
