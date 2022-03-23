package com.alten.jwtexercise.service.product;

import com.alten.jwtexercise.domain.Product;

import java.util.Set;

public interface ProductService {

    Product saveProduct(Product product);
    Product getProductById(Long id);
    Product getProductByProductName(String productName);
    Set<Product> getProductsByCategory(String category);
    Set<Product> getProducts();
    Product updateProduct(Product product);
    void deleteProductById(Long id);
    void addDetailToProduct(Long detailId, Long productId);

}
