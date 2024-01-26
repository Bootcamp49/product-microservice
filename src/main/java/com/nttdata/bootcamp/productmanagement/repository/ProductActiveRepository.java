package com.nttdata.bootcamp.productmanagement.repository;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Interfaz para el repositorio de productos activos.
 */
public interface ProductActiveRepository extends ReactiveCrudRepository<ProductActive, String> {

    Flux<ProductActive> findByClientId(String clientId);
    
}
