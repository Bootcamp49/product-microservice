package com.nttdata.bootcamp.productmanagement.proxy;

import com.nttdata.bootcamp.productmanagement.model.Movement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz para los metodos de movimientos.
 */
public interface MovementProxy {
    Mono<Movement> createMovement(Movement movement);

    Flux<Movement> reportCommission(String productId, Integer productTypeId);
    
    Flux<Movement> reportMovements(String productId, Integer productTypeId);

    Flux<Movement> getMovementsByProductId(String productId);
}
