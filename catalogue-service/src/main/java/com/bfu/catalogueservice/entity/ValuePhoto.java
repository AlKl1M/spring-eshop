package com.bfu.catalogueservice.entity;

public enum ValuePhoto {
    Preview,
    Catalogue;
    public static String checkValue(boolean flag) {
        return flag ? Preview.toString() : Catalogue.toString();
    }
}
