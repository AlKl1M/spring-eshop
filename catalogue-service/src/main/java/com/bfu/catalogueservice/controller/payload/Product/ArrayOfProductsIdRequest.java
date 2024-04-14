package com.bfu.catalogueservice.controller.payload.Product;

import java.util.ArrayList;

public record ArrayOfProductsIdRequest(
        ArrayList<String> productsId
) {
}
