package com.alten.jwtexercise.rest;

import com.alten.jwtexercise.domain.Product;
import com.alten.jwtexercise.domain.Store;
import com.alten.jwtexercise.exception.MyCustomException;
import com.alten.jwtexercise.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class StoreRestController {

    private final StoreService storeService;

    @Autowired
    public StoreRestController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<Set<Store>> getStores(){
        return ResponseEntity.ok().body(storeService.getStores());
    }

    @GetMapping("/stores/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<Store> getStoreById(@PathVariable Long storeId){
        Store store = storeService.getStoreById(storeId);
        if(store==null){
            throw new MyCustomException("Store with id '"+storeId+"' not found!");
        }
        return ResponseEntity.ok().body(store);
    }

    @GetMapping("/stores/retireveByStoreName/{storeName}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<Store> getStoreByStoreName(@PathVariable String storeName){
        Store store = storeService.getStoreByStoreName(storeName);
        if(store==null){
            throw new MyCustomException("Store '"+storeName+"' not found!");
        }
        return ResponseEntity.ok().body(store);
    }

    @GetMapping("/stores/viewProducts/{storeName}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<Set<Product>> productsPerStore(@PathVariable String storeName){
        Set<Product> products = storeService.getProductsByStore(storeName);
        if(products.stream().noneMatch(
                product -> product.getStores().stream().noneMatch(
                        store -> store.getStoreName().equals(storeName)))){
            throw new MyCustomException("There are no products stored in '"+storeName+"'");
        }
        return ResponseEntity.ok().body(products);
    }

    @PostMapping("/stores")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<Store> saveStore(@RequestBody Store store){
        return ResponseEntity.ok().body(storeService.saveStore(store));
    }

    @PostMapping("/stores/addProduct/{productId}/toStore/{storeName}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<String> addProductToStore(@PathVariable Long productId, @PathVariable String storeName){
        storeService.addProductToStore(productId, storeName);
        return ResponseEntity.ok().body("Product '"+productId+"' added to store '"+storeName+"'");
    }

    @PutMapping("/stores")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<Store> updateStore(@RequestBody Store store){
        return ResponseEntity.ok().body(storeService.updateStore(store));
    }

    @DeleteMapping("/stores/{storeId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MKT')")
    public ResponseEntity<String> deleteStoreById(@PathVariable Long storeId){
        Store store = storeService.getStoreById(storeId);
        if(store==null){
            throw new MyCustomException("Store with id '"+storeId+"' not found!");
        }
        storeService.deleteStoreById(storeId);
        return ResponseEntity.ok().body("Store with id '"+storeId+"' deleted");
    }

}
