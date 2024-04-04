package com.bfu.catalogueservice.service.brand;

import com.bfu.catalogueservice.controller.payload.Brand.BrandResponse;
import com.bfu.catalogueservice.controller.payload.Brand.CreateBrandRequest;
import com.bfu.catalogueservice.controller.payload.Brand.DeleteBrandRequest;
import com.bfu.catalogueservice.controller.payload.Brand.UpdateBrandRequest;
import com.bfu.catalogueservice.controller.payload.Category.CategoryResponse;
import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.entity.Category;
import com.bfu.catalogueservice.entity.Product;
import com.bfu.catalogueservice.exception.BrandNotFoundException;
import com.bfu.catalogueservice.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    @Override
    public void createBrand(CreateBrandRequest createBrandRequest) {
        Brand brand = Brand.builder()
                .brandId(UUID.randomUUID().toString().substring(0,15))
                .name(createBrandRequest.name())
                .build();
        brandRepository.save(brand);
    }

    @Override
    public void deleteBrand(DeleteBrandRequest brandRequest) {
        Brand brand = brandRepository.findByBrandId(brandRequest.brandId());
        if (brand != null){
            brandRepository.delete(brand);
        }
        else {
            throw new BrandNotFoundException(brandRequest.brandId());
        }
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        List<BrandResponse> brands = new ArrayList<>();
        for (Brand brand: brandRepository.findAll()){
            brands.add(BrandResponse.from(brand));
        }
        return brands;
    }

    @Override
    public void updateBrand(UpdateBrandRequest brandRequest) {
        Brand brand = brandRepository.findByBrandId(brandRequest.brandId());
        if (brand != null){
            brand.setName(brandRequest.newName());
            brandRepository.save(brand);
        }
        else {
            throw new BrandNotFoundException(brandRequest.brandId());
        }
    }
}
