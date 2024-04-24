package com.bfu.catalogueservice.controller;

import com.bfu.catalogueservice.controller.payload.Brand.BrandResponse;
import com.bfu.catalogueservice.controller.payload.Brand.CreateBrandRequest;
import com.bfu.catalogueservice.controller.payload.Brand.UpdateBrandRequest;
import com.bfu.catalogueservice.entity.Brand;
import com.bfu.catalogueservice.service.brand.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/catalogue")
@Slf4j
public class BrandController {
    private final BrandService brandService;
    @GetMapping("/getAllBrands")
    public List<BrandResponse> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping("/create-brand")
    public ResponseEntity<?> createBrand(@RequestBody @Valid CreateBrandRequest brandRequest){
        brandService.createBrand(brandRequest);
        log.info("Brand has been created");
        return ResponseEntity.ok("Brand has been created");
    }
    @PutMapping("/update-brand")
    public ResponseEntity<?> updateBrand(@RequestBody @Valid UpdateBrandRequest brandRequest){
        brandService.updateBrand(brandRequest);
        log.info("Brand has been updated");
        return ResponseEntity.ok("Brand has been updated");
    }
    @DeleteMapping("/delete-brand")
    public ResponseEntity<?> deleteBrand(@RequestParam String brandId) {
        brandService.deleteBrand(brandId);
        log.info("Brand has been deleted");
        return ResponseEntity.noContent().build();
    }
}
