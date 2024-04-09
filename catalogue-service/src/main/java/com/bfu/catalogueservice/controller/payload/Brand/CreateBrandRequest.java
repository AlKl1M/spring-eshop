package com.bfu.catalogueservice.controller.payload.Brand;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBrandRequest(
        @NotBlank
        @NotNull
        @Size(max = 20)
        String name
) {
}
