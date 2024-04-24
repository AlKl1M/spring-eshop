package com.bfu.catalogueservice.controller.payload.Brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBrandRequest(
        @NotBlank
        @NotNull
        @Size(max = 20)
        String newName,
        @Size(max = 15, min = 15)
        String brandId
) {
}
