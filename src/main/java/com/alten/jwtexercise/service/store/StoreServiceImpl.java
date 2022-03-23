package com.alten.jwtexercise.service.store;

import com.alten.jwtexercise.domain.Product;
import com.alten.jwtexercise.domain.Store;
import com.alten.jwtexercise.repositories.ProductRepository;
import com.alten.jwtexercise.repositories.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, ProductRepository productRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    //utility method: check if store already exists
    public boolean checkStore(Store store){
        List<Store> stores = storeRepository.findAll();
        if(stores.stream().anyMatch(s -> (s.getStoreName().equals(store.getStoreName())))){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Store saveStore(Store store) {
        log.info("Saving new store '{}' to database...", store.getStoreName());
        if(checkStore(store)){
            throw new RuntimeException("Store '"+store.getStoreName()+"' already exists");
        }
        store.setId(0L);
        return storeRepository.save(store);
    }

    @Override
    public Store getStoreById(Long id) {
        log.info("Getting store with id '{}' from database...", id);
        Optional<Store> optionalStore = storeRepository.findById(id);
        Store store = null;
        if(optionalStore.isPresent()){
            store=optionalStore.get();
        }
        return store;
    }

    @Override
    public Store getStoreByStoreName(String storeName) {
        log.info("Getting store '{}' from database...", storeName);
        Optional<Store> optionalStore = storeRepository.findByStoreName(storeName);
        Store store = null;
        if(optionalStore.isPresent()){
            store = optionalStore.get();
        }
        return store;
    }

    @Override
    public Set<Product> getProductsByStore(String storeName) {
        log.info("Getting store '{}' from database...", storeName);
        Optional<Store> optionalStore = storeRepository.findByStoreName(storeName);
        Store store;
        Set<Product> products = new HashSet<>();
        if(optionalStore.isPresent()){
            store = optionalStore.get();
            products = store.getProducts();
        }
        return products;
    }

    @Override
    public Set<Store> getStores() {
        log.info("Getting all stores from database...");
        return storeRepository.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public Store updateStore(Store store) {
        log.info("Updating store '{}' from database...", store.getStoreName());
        if(checkStore(store)){
            log.error("Error! No changes have been made");
            throw new RuntimeException("Store '"+store.getStoreName()+"' already exists. No changes have been made");
        }else{
            return storeRepository.saveAndFlush(store);
        }
    }

    @Override
    public void deleteStoreById(Long id) {
        log.info("Deleting store with id '{}' from database...", id);
        storeRepository.deleteById(id);
    }

    //metodo che aggiunge un oggetto product esistente a uno store già esistente
    @Override
    public void addProductToStore(Long productId, String storeName) {
        log.info("Getting product '{}' and store '{}' from database...", productId, storeName);
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Optional<Store> optionalStore = storeRepository.findByStoreName(storeName);
        Product product;
        Store store;
        if(optionalProduct.isPresent() && optionalStore.isPresent()){
            log.info("Adding product '{}' to store '{}'...", productId, storeName);
            product = optionalProduct.get();
            store = optionalStore.get();
            store.getProducts().add(product);
        }else{
            log.error("Error! Product or Store not found in the database");
            throw new RuntimeException("Product id or store name doesn't exists");
        }
    }
}
