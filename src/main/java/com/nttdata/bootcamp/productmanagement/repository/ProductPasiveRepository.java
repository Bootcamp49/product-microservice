package com.nttdata.bootcamp.productmanagement.repository;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


/**
 * Interfaz para el repositorio de Productos pasivos.
 */
public interface ProductPasiveRepository extends ReactiveMongoRepository<ProductPasive, String> {
    Flux<ProductPasive> findByClientId(String clientId);

    Flux<ProductPasive> findByDebitCardNumber(String debitCardNumber);
    
}
