package com.bfu.catalogueservice.service.brand;

import com.bfu.catalogueservice.controller.payload.Brand.BrandResponse;
import com.bfu.catalogueservice.controller.payload.Brand.CreateBrandRequest;
import com.bfu.catalogueservice.controller.payload.Brand.UpdateBrandRequest;
import com.bfu.catalogueservice.entity.Brand;

import java.util.List;

public interface BrandService {
    void createBrand(CreateBrandRequest createBrandRequest);

    void deleteBrand(String brandId);

    List<BrandResponse> getAllBrands();

    void updateBrand(UpdateBrandRequest brandRequest);
}
