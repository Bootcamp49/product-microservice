package com.nttdata.bootcamp.productmanagement.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;

import reactor.core.publisher.Flux;

public interface ProductPasiveRepository extends ReactiveCrudRepository<ProductPasive, String> {

    Flux<ProductPasive> findByClientId(String clientId);
    
}
