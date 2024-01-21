package com.nttdata.bootcamp.productmanagement.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;

import reactor.core.publisher.Flux;

public interface ProductActiveRepository extends ReactiveCrudRepository<ProductActive, String>{

    Flux<ProductActive> findByClientId(String clientId);
    
}
