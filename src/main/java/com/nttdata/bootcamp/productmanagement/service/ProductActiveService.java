package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductActiveService {
    
    /**
     * @return Retorno de todos los productos activos
     */
    Flux<ProductActive> findProducts();
    
    /**
     * @param id Id del producto activo a retornar
     * @return Retorna un producto especifico
     */
    Mono<ProductActive> findById(String id);

    /**
     * @param clientId Id del cliente del cual retornar sus productos activos
     * @return Retorna los productos activos que tiene un cliente
     */
    Flux<ProductActive> findByClientId(String clientId);

    /**
     * @param productActive Cuerpo del producto activo a crear
     * @return Retorna el producto creado
     */
    Mono<ProductActive> createProduct(ProductActive productActive);

    /**
     * @param id Id del producto activo a actualizar
     * @param productActive Cuerpo del producto activo a actualizar
     * @return Retorno del producto activo actualizado
     */
    Mono<ProductActive> updateProduct(String id, ProductActive productActive);

    /**
     * @param id Id del producto a eliminar
     * @return Retorna un Void de eliminacion del producto
     */
    Mono<Void> deleteProduct(String id);

    /**
     * @param id Id del producto del cual se va a consumir su credito
     * @param consumeAmount Monto a consumir del producto
     * @return Retorno del credito actual luego del consumo
     */
    Mono<Double> consumeCredit(String id, Double consumeAmount);

    /**
     * @param id Id del producto del cual se va a pagar su credito
     * @param payAmount Monto a pagar del producto
     * @return Retorno del credito actual luego del consumo
     */
    Mono<Double> payCredit(String id, Double payAmount);
}
