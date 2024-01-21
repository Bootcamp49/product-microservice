package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductActiveService {
    
    Flux<ProductActive> findProducts();
    
    Mono<ProductActive> findById(String id);

    Flux<ProductActive> findByClientId(String clientId);

    Mono<ProductActive> createProduct(ProductActive productActive);

    Mono<ProductActive> updateProduct(String id, ProductActive productActive);

    Mono<Void> deleteProduct(String id);
}
