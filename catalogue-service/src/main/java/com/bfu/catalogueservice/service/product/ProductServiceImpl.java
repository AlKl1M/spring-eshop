package com.bfu.catalogueservice.service.product;

import com.bfu.catalogueservice.controller.payload.Product.*;
import com.bfu.catalogueservice.controller.payload.ProductPhoto.CreateProductPhotoResponse;
import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.exception.BrandNotFoundException;
import com.bfu.catalogueservice.exception.CategoryNotFoundException;
import com.bfu.catalogueservice.exception.ProductNotFoundException;
import com.bfu.catalogueservice.repository.BrandRepository;
import com.bfu.catalogueservice.repository.CategoryRepository;
import com.bfu.catalogueservice.repository.ProductRepository;
import com.bfu.catalogueservice.service.image.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ImageService imageService;
    @Override
    @Transactional
    public CreateProductPhotoResponse createProduct(CreateProductRequest productRequest) {
        log.info("Start creating product with name {}", productRequest.name());
        Optional<Brand> brand = brandRepository.findByBrandId(productRequest.brandId());
        Optional<Category> category = categoryRepository.findByCategoryId(productRequest.categoryId());
        imageService.createBufferedImages(productRequest.images());
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
                return CreateProductPhotoResponse.from(
                        product,
                        imageService.createBufferedImages(productRequest.images())
                );
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
    public List<Product> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public void updateProduct(UpdateProductRequest productRequest) {
        log.info("Start updating product with name {}", productRequest.newName());
        Optional<Brand> brand = brandRepository.findByBrandId(productRequest.newBrandId());
        Optional<Category> category = categoryRepository.findByCategoryId(productRequest.newCategoryId());
        Optional<Product> optionalProduct = productRepository.findByProductId(productRequest.productId());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
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

    }

    @Override
    @Transactional
    public void deleteProduct(String productId) {
        log.info("Start deleting product with productId {}", productId);
        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
        }
        else {
            log.error("Product not found with productId {}", productId);
            throw new ProductNotFoundException(productId);
        }
    }

    @Override
    public SimplifiedProductResponse getSimpleProductById(String productId, String photo) {
        log.info("Getting Simplified product with productId {}", productId);
        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isPresent()) {
            return SimplifiedProductResponse.from(optionalProduct.get(), photo);
        }
        log.error("Product not found with productId {}", productId);
        throw new ProductNotFoundException(productId);
    }

    @Override
    public FullProductResponse getFullProductById(String productId, List<String> photos) {
        log.info("Getting Full product with productId {}", productId);
        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isPresent()) {
            return FullProductResponse.from(optionalProduct.get(), photos);
        }
        log.error("Product not found with productId {}", productId);
        throw new ProductNotFoundException(productId);
    }

    @Override
    public List<Product> getArraySimpleProductsById(List<String> productsId) {
        log.info("Getting Array products where productId in {}", productsId);
        return productRepository.findAllByProductIdIn(productsId);
    }

    @Override
    public List<Product> getArrayFullProductsById(List<String> productsId) {
        log.info("Getting Array products where productId in {}", productsId);
        return productRepository.findAllByProductIdIn(productsId);
    }
    @Override
    public boolean isProductExists(String productId) {
        return productRepository.existsProductByProductId(productId);
    }
}
