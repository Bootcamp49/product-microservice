package com.nttdata.bootcamp.productmanagement.proxy;

import com.nttdata.bootcamp.productmanagement.model.Movement;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase de la implementación de la interfaz de MovementProxy.
 */
@RequiredArgsConstructor
@Service
public class MovementProxyImpl implements MovementProxy {
    @Autowired
    final private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Movement> createMovement(@NonNull Movement movement) {
        Mono<Movement> response = webClientBuilder.build().post()
                .uri("http://movement-service")
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
        WebClient webClient = webClientBuilder.build();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(
                "http://movement-service/report/commission/{productId}")
                .queryParam("productTypeId", productTypeId);

        Flux<Movement> response = webClient.get()
                .uri(uriBuilder.build(productId))
                .retrieve()
                .bodyToFlux(Movement.class);
        return response;
    }

    @Override
    public Flux<Movement> reportMovements(String productId, Integer productTypeId) {
        WebClient webClient = webClientBuilder.build();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(
                "http://movement-service/report/{productId}"
        ).queryParam("productTypeId", productTypeId);

        Flux<Movement> response = webClient.get()
                .uri(uriBuilder.build(productId))
                .retrieve()
                .bodyToFlux(Movement.class);
        return response;
    }

    @Override
    public Flux<Movement> getMovementsByProductId(String productId) {
        WebClient webClient = webClientBuilder.build();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(
                "http://movement-service/product/{productId}");

        Flux<Movement> response = webClient.get()
                .uri(uriBuilder.build(productId))
                .retrieve()
                .bodyToFlux(Movement.class);
        return response;
    }

    @Override
    public Flux<Movement> getMovementReportByCard(String productsId, Integer productType) {
        WebClient webClient = webClientBuilder.build();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(
                "http://movement-service/report/card")
                .queryParam("productTypeId", productType)
                .queryParam("productsId", String.join(",", productsId));

        Flux<Movement> response = webClient.get()
                .uri(uriBuilder.build().toUri())
                .retrieve()
                .bodyToFlux(Movement.class);
        return response;
    }

}
