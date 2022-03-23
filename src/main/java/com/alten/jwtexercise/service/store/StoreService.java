package com.alten.jwtexercise.service.store;

import com.alten.jwtexercise.domain.Product;
import com.alten.jwtexercise.domain.Store;

import java.util.Set;

public interface StoreService {

    Store saveStore(Store store);
    Store getStoreById(Long id);
    Store getStoreByStoreName(String storeName);
    Set<Store> getStores();
    Store updateStore(Store store);
    void deleteStoreById(Long id);
    void addProductToStore(Long productId, String storeName);
    Set<Product> getProductsByStore(String storeName);
}
