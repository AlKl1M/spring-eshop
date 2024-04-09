package com.bfu.catalogueservice.service.brand;

import com.bfu.catalogueservice.controller.payload.Brand.BrandResponse;
import com.bfu.catalogueservice.controller.payload.Brand.CreateBrandRequest;
import com.bfu.catalogueservice.controller.payload.Brand.UpdateBrandRequest;
import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.exception.BrandNotFoundException;
import com.bfu.catalogueservice.repository.BrandRepository;
import com.bfu.catalogueservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    @Override
    public void createBrand(CreateBrandRequest createBrandRequest) {
        log.info("Start creating brand with name {}", createBrandRequest.name());
        Brand brand = Brand.builder()
                .brandId(UUID.randomUUID().toString().substring(0,15))
                .name(createBrandRequest.name())
                .build();
        brandRepository.save(brand);
    }

    @Override
    public void deleteBrand(String brandId) {
        log.info("Start delete brand with BrandId {}", brandId);
        Brand brand = brandRepository.findByBrandId(brandId);
        if (brand != null){
            productRepository.deleteAll(productRepository.findAllByBrand(brand));
            brandRepository.delete(brand);
        }
        else {
            log.error("Brand not found with brandId {}", brandId);
            throw new BrandNotFoundException(brandId);
        }
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        log.info("Getting All Brands");
        List<BrandResponse> brands = new ArrayList<>();
        for (Brand brand: brandRepository.findAll()){
            brands.add(BrandResponse.from(brand));
        }
        return brands;
    }

    @Override
    public void updateBrand(UpdateBrandRequest brandRequest) {
        log.info("Start updating brand with name {}", brandRequest.newName());
        Brand brand = brandRepository.findByBrandId(brandRequest.brandId());
        if (brand != null){
            brand.setName(brandRequest.newName());
            brandRepository.save(brand);
        }
        else {
            log.error("Brand not found with brandId {}", brandRequest.brandId());
            throw new BrandNotFoundException(brandRequest.brandId());
        }
    }
}
