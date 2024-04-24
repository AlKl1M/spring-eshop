package com.bfu.catalogueservice.controller.payload.ProductPhoto;

import java.awt.*;
import java.util.ArrayList;

public record CreateProductImages(
        ArrayList<Byte> images
) {
}
