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
@FeignClient(name="movement-service")
public interface MovementProxy {
    @PostMapping()
    Mono<Movement> createMovement(Movement movement);

    @GetMapping("/report/commission/{productId}")
    Flux<Movement> reportCommission(@PathVariable String productId, @SpringQueryMap Integer productTypeId);

    @GetMapping("/report/{productId}")
    Flux<Movement> reportMovements(@PathVariable String productId, @SpringQueryMap Integer productTypeId);

    @GetMapping("/product/{productId}")
    Flux<Movement> getMovementsByProductId(@PathVariable String productId);

    @GetMapping("/report/card")
    Flux<Movement> getMovementReportByCard(@SpringQueryMap String productsId, @SpringQueryMap Integer productType);
}
