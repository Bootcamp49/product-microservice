package com.nttdata.bootcamp.productmanagement.proxy;

import com.nttdata.bootcamp.productmanagement.model.Movement;
import java.util.List;

import feign.Param;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz para los metodos de movimientos.
 */
public interface MovementProxy {
    Mono<Movement> createMovement(Movement movement);

    Flux<Movement> reportCommission(@PathVariable String productId, @SpringQueryMap Integer productTypeId);

    Flux<Movement> reportMovements(@PathVariable String productId, @SpringQueryMap Integer productTypeId);

    Flux<Movement> getMovementsByProductId(@PathVariable String productId);

    Flux<Movement> getMovementReportByCard(@SpringQueryMap String productsId, @SpringQueryMap Integer productType);
}
