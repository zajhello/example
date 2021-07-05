package com.dxmovie.dxbase.core;




/**
 * @Description
 */
public class BaseConfig {
    public static int ENV = Environment.DEV;

    public static boolean isDEV() {
        return ENV == Environment.DEV;
    }

    public static boolean isProduct() {
        return ENV == Environment.PRODUCT;
    }

}
