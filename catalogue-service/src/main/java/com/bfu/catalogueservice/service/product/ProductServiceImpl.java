package com.bfu.catalogueservice.service.product;

import com.bfu.catalogueservice.controller.payload.Product.*;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.entity.ProductPhoto;
import com.bfu.catalogueservice.exception.BrandNotFoundException;
import com.bfu.catalogueservice.exception.CanNotReadPhotoException;
import com.bfu.catalogueservice.exception.CategoryNotFoundException;
import com.bfu.catalogueservice.exception.ProductNotFoundException;
import com.bfu.catalogueservice.repository.BrandRepository;
import com.bfu.catalogueservice.repository.CategoryRepository;
import com.bfu.catalogueservice.repository.ProductPhotoRepository;
import com.bfu.catalogueservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.apache.commons.lang3.BooleanUtils.forEach;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    @Override
    public CreateProductPhotoResponse createProduct(CreateProductRequest productRequest) {
        log.info("Start creating product with name {}", productRequest.name());
        Optional<Brand> brand = brandRepository.findByBrandId(productRequest.brandId());
        Optional<Category> category = categoryRepository.findByCategoryId(productRequest.categoryId());
        List<BufferedImage> images = productRequest.images().stream()
                .map(p-> {
                    try {
                        return ImageIO.read(new ByteArrayInputStream(p));
                    } catch (IOException e) {
                        throw CanNotReadPhotoException.of(p);
                    }
                }).toList();
        if (brand.isPresent()) {
            if (category.isPresent()) {
                Product product =
                        Product.builder()
                        .productId(UUID.randomUUID().toString().substring(0,15))
                        .price(productRequest.price())
                        .name(productRequest.name())
                        .attributes(productRequest.attributes())
                        .description(productRequest.description())
                        .brand(brand.get())
                        .category(category.get())
                        .build();
                productRepository.save(product);
                return CreateProductPhotoResponse.from(product, images);
            }
            else {
                log.error("Category not found with categoryId {}", productRequest.categoryId());
                throw new CategoryNotFoundException(productRequest.categoryId());
            }
        }
        else {
            log.error("Brand not found with brandId {}", productRequest.brandId());
            throw new BrandNotFoundException(productRequest.brandId());
        }
    }

    @Override
    public List<FullProductResponse> getAllProducts() {
        log.info("Getting all products");
        List<FullProductResponse> products = new ArrayList<>();
        for (Product product: productRepository.findAll()){
            products.add(FullProductResponse.from(product));
        }
        return products;
    }

    @Override
    public void updateProduct(UpdateProductRequest productRequest) {
        log.info("Start updating product with name {}", productRequest.newName());
        Optional<Brand> brand = brandRepository.findByBrandId(productRequest.newBrandId());
        Optional<Category> category = categoryRepository.findByCategoryId(productRequest.newCategoryId());
        Product product = productRepository.findByProductId(productRequest.productId());
        brand.ifPresent(product::setBrand);
        category.ifPresent(product::setCategory);
        if (productRequest.newPrice() != null) {
            product.setPrice(productRequest.newPrice());
        }
        if (productRequest.newName() != null) {
            product.setName(productRequest.newName());
        }
        if (productRequest.description() != null) {
            product.setDescription(productRequest.description());
        }
        productRepository.save(product);

    }

    @Override
    public void deleteProduct(String productId) {
        log.info("Start deleting product with productId {}", productId);
        Product product = productRepository.findByProductId(productId);
        if (product != null) {
            productRepository.delete(product);
        }
        else {
            log.error("Product not found with productId {}", productId);
            throw new ProductNotFoundException(productId);
        }
    }

    @Override
    public SimplifiedProductResponse getSimpleProductById(String productId) {
        log.info("Getting Simplified product with productId {}", productId);
        Optional<Product> result = Optional.ofNullable(productRepository.findByProductId(productId));
        if (result.isPresent()) {
            Product product = result.get();
            return new SimplifiedProductResponse(
                    product.getProductId(),
                    product.getName(),
                    product.getPrice()
            );
        }
        log.error("Product not found with productId {}", productId);
        throw new ProductNotFoundException(productId);
    }

    @Override
    public FullProductResponse getFullProductById(String productId) {
        log.info("Getting Full product with productId {}", productId);
        Optional<Product> result = Optional.ofNullable(productRepository.findByProductId(productId));
        if (result.isPresent()) {
            Product product = result.get();
            return new FullProductResponse(
                    product.getProductId(),
                    product.getName(),
                    product.getPrice(),
                    product.getAttributes(),
                    product.getDescription(),
                    product.getCategory(),
                    product.getBrand());
        }
        log.error("Product not found with productId {}", productId);
        throw new ProductNotFoundException(productId);
    }

    @Override
    public ArrayList<SimplifiedProductResponse> getArraySimpleProductsById(List<String> productsId) {
        log.info("Getting Array Simplified products by productsId {}", productsId);
        ArrayList<SimplifiedProductResponse> products = new ArrayList<>();
        for (String productId: productsId){
            Optional<Product> result = Optional.ofNullable(productRepository.findByProductId(productId));
            if (result.isPresent()){
                Product product = result.get();
                products.add(new SimplifiedProductResponse(
                        product.getProductId(),
                        product.getName(),
                        product.getPrice()
                ));
            }
        }
        return products;
    }

    @Override
    public ArrayList<FullProductResponse> getArrayFullProductsById(List<String> productsId) {
        log.info("Getting Array Full products by productsId {}", productsId);
        ArrayList<FullProductResponse> products = new ArrayList<>();
        for (String productId: productsId){
            Optional<Product> result = Optional.ofNullable(productRepository.findByProductId(productId));
            if (result.isPresent()){
                Product product = result.get();
                products.add(new FullProductResponse(
                        product.getProductId(),
                        product.getName(),
                        product.getPrice(),
                        product.getAttributes(),
                        product.getDescription(),
                        product.getCategory(),
                        product.getBrand()
                ));
            }
        }
        return products;
    }
    @Override
    public boolean isProductExists(String productId) {
        return productRepository.existsProductByProductId(productId);
    }
}
