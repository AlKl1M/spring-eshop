package com.bfu.catalogueservice.controller.payload.Product;

import java.util.List;

public record ArrayOfProductsIdRequest(
        List<String> productsId
) {
    public static ArrayOfProductsIdRequest from(List<String> productsId){
        return new ArrayOfProductsIdRequest(
                productsId
        );
    }
}
