package com.bfu.feedbackservice.controller.payload;

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
