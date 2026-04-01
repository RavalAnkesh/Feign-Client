package com.example.product_service.servicei;

import com.example.product_service.entity.Product;
import java.util.List;

public interface ProductServiceI {

    Product createProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}