package com.bfu.catalogueservice.controller;

import com.bfu.catalogueservice.controller.payload.Brand.BrandResponse;
import com.bfu.catalogueservice.controller.payload.Brand.CreateBrandRequest;
import com.bfu.catalogueservice.controller.payload.Brand.DeleteBrandRequest;
import com.bfu.catalogueservice.controller.payload.Brand.UpdateBrandRequest;
import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.service.brand.BrandService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/catalogue")
public class BrandController {
    private final BrandService brandService;
    @GetMapping("/getAllBrands")
    public List<BrandResponse> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping("/create-brand")
    public ResponseEntity<?> createBrand(@RequestBody @Valid CreateBrandRequest brandRequest){
        brandService.createBrand(brandRequest);
        return ResponseEntity.ok("Brand has been created");
    }
    @PutMapping("/update-brand")
    public ResponseEntity<?> updateBrand(@RequestBody UpdateBrandRequest brandRequest){
        brandService.updateBrand(brandRequest);
        return ResponseEntity.ok("Brand has been updated");
    }
    @DeleteMapping("/delete-brand")
    public void deleteBrand(@RequestBody DeleteBrandRequest deleteBrandRequest) {
        brandService.deleteBrand(deleteBrandRequest);
    }
}
