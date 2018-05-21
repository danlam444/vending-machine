package com.lammyhill.vendingmachine;

import java.util.ArrayList;

public interface iInventory {
    boolean takeProduct(Product product);

    boolean addProduct(Product product);

    ArrayList<Product> getSupportedProducts() ;

    void setSupportedProducts(ArrayList<Product> supportedProducts);

    boolean isInStock(Product product);

    ArrayList<Product> loadSupportedProducts();
}
