package com.alten.jwtexercise.rest;

import com.alten.jwtexercise.domain.Product;
import com.alten.jwtexercise.enums.Categories;
import com.alten.jwtexercise.exception.MyCustomException;
import com.alten.jwtexercise.service.product.ProductService;
import com.alten.jwtexercise.service.product.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<Set<Product>> getProducts(){
        return ResponseEntity.ok().body(productService.getProducts());
    }

    @GetMapping("/products/{productId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId){
        Product product = productService.getProductById(productId);
        if(product==null){
            throw new MyCustomException("Product with '"+productId+"' not found!");
        }
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/products/retireveByProductName/{productName}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<Product> getProductByProductName(@PathVariable String productName){
        Product product = productService.getProductByProductName(productName);
        if(product==null){
            throw new MyCustomException("Product '"+productName+"' not found!");
        }
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/products/retrieveByCategory/{category}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<Set<Product>> getProductsByCategory(@PathVariable String category){
        Set<Product> products = productService.getProductsByCategory(category);
        if(products.stream().noneMatch(product -> product.getDetail().getCategory().equals(Categories.valueOf(category)))){
            throw new MyCustomException("Category '"+category+"' doesn't exists");
        }
        return ResponseEntity.ok().body(products);
    }

    @PostMapping("/products")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){
        return ResponseEntity.ok().body(productService.saveProduct(product));
    }

    @PostMapping("/products/addDetail/{detailId}/toProduct/{productId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<String> addDetailToUser(@PathVariable Long detailId, @PathVariable Long productId){
        productService.addDetailToProduct(detailId, productId);
        return ResponseEntity.ok().body("Detail '"+detailId+"' added to product '"+productId+"'");
    }

    @PutMapping("/products")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product){
        return ResponseEntity.ok().body(productService.updateProduct(product));
    }

    @DeleteMapping("/products/{productId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ICM')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        Product product = productService.getProductById(productId);
        if(product==null){
            throw new MyCustomException("Product with id '"+productId+"' not found!");
        }
        productService.deleteProductById(productId);
        return ResponseEntity.ok().body("Product with id '"+productId+"' deleted");
    }


}
