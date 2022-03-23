package com.alten.jwtexercise.service.product;

import com.alten.jwtexercise.domain.Detail;
import com.alten.jwtexercise.domain.Product;
import com.alten.jwtexercise.enums.Categories;
import com.alten.jwtexercise.repositories.DetailRepository;
import com.alten.jwtexercise.repositories.ProductRepository;
import com.alten.jwtexercise.repositories.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final DetailRepository detailRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, DetailRepository detailRepository, StoreRepository storeRepository) {
        this.productRepository = productRepository;
        this.detailRepository = detailRepository;
        this.storeRepository = storeRepository;
    }

    //Utility method: check if product already exists
    public boolean checkProduct(Product product){
        List<Product> products = productRepository.findAll();
        if(products.stream().anyMatch(p -> (p.getProductName().equals(product.getProductName())))){
            return true;
        }else{
            return false;
        }
    }

    //Utility method: check if category exists
    public boolean checkCategory(Categories category){
        List<Categories> categories = List.of(Categories.values());
        if(categories.contains(category)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Product saveProduct(Product product) {
        log.info("Saving new product '{}' to database...", product);
        if(checkProduct(product)){
            log.error("Error! product '{}' already exists",product.getProductName());
            throw new RuntimeException("Product '"+product.getProductName()+"' already exists");
        }
        product.setId(0L);
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        log.info("Getting product with id '{}' from database...", id);
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = null;
        if(optionalProduct.isPresent()){
            product=optionalProduct.get();
        }
        return product;
    }

    @Override
    public Product getProductByProductName(String productName) {
        log.info("Getting product '{}' from database...", productName);
        Optional<Product> optionalProduct = productRepository.findByProductName(productName);
        Product product = null;
        if(optionalProduct.isPresent()){
            product=optionalProduct.get();
        }
        return product;
    }

    @Override
    public Set<Product> getProductsByCategory(String category) {
        log.info("Getting all '{}' products...", category);
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream().filter(product -> (
                     product.getDetail().getCategory().equals(Categories.valueOf(category))))
                     .collect(Collectors.toSet());
    }

    @Override
    public Set<Product> getProducts() {
        log.info("Getting all product from database...");
        return productRepository.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public Product updateProduct(Product product) {
        log.info("Updating product '{}'...", product.getProductName());
        if(checkProduct(product)){
            log.error("Error! No changes have been made");
            throw new RuntimeException("Product '"+product.getProductName()+"' already exists. No changes have been made");
        }else{
            return  productRepository.saveAndFlush(product);
        }
    }

    @Override
    public void deleteProductById(Long id) {
        log.info("Deleting product with id '{}' from database...", id);
        productRepository.deleteById(id);
    }

    //metodo che aggiunge un oggetto detail già esistente a un product già esistente
    @Override
    public void addDetailToProduct(Long detailId, Long productId) {
        log.info("Getting detail '{}' and product '{}' from database...", detailId, productId);
        Optional<Detail> optionalDetail = detailRepository.findById(detailId);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Detail detail;
        Product product;
        if(optionalDetail.isPresent() && optionalProduct.isPresent()){
            log.info("Adding detail '{}' to product '{}'...", detailId, productId);
            detail=optionalDetail.get();
            product=optionalProduct.get();
            product.setDetail(detail);
        }else{
            log.error("Error! Detail or product not found in the database");
            throw new RuntimeException("Detail id or product id doesn't exists");
        }
    }
}
