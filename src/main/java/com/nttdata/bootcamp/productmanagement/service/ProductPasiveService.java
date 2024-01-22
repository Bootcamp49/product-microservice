package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPasiveService {

    Flux<ProductPasive> findProducts();
    
    Mono<ProductPasive> findById(String id);
    
    Flux<ProductPasive> findByClientId(String clientId);

    Mono<ProductPasive> createProduct(ProductPasive product);

    Mono<ProductPasive> updateProduct(String id, ProductPasive product);

    Mono<Void> deleteProduct(String id);

    Mono<ProductPasive> debitMovement(String id, Double debitAmount);

    Mono<ProductPasive> depositMovement(String id, Double depositAmount);
}
