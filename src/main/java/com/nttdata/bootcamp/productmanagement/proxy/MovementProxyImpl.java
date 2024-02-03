package com.nttdata.bootcamp.productmanagement.proxy;

import com.nttdata.bootcamp.productmanagement.model.Movement;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase de la implementación de la interfaz de MovementProxy.
 */
@RequiredArgsConstructor
@Service
public class MovementProxyImpl implements MovementProxy {

    private WebClient webClient = WebClient.create("http://localhost:8082");

    @Override
    public Mono<Movement> createMovement(@NonNull Movement movement) {
        Mono<Movement> response = webClient.post()
                .uri("/movement")
                .body(BodyInserters.fromValue(movement))
                .exchangeToMono(movementResponse -> {
                    if (movementResponse.statusCode().equals(HttpStatus.OK)) {
                        return movementResponse.bodyToMono(Movement.class);
                    } else {
                        return movementResponse.createError();
                    }
                });
        return response;
    }

    @Override
    public Flux<Movement> reportCommission(String productId, Integer productTypeId) {
        Flux<Movement> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/movement/report/commission/{productId}")
                    .queryParam("productTypeId", productTypeId)
                    .build(productId))
                .retrieve()
                .bodyToFlux(Movement.class);
        return response;
    }

    @Override
    public Flux<Movement> reportMovements(String productId, Integer productTypeId) {
        Flux<Movement> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/movement/report/{productId}")
                    .queryParam("productTypeId", productTypeId)
                    .build(productId))
                .retrieve()
                .bodyToFlux(Movement.class);
        return response;
    }

    @Override
    public Flux<Movement> getMovementsByProductId(String productId) {
        Flux<Movement> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/movement/product/{productId}")
                    .build(productId)
                    )
                .retrieve()
                .bodyToFlux(Movement.class);
        return response;
    }
}
