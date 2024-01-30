package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz para los productos activos.
 */
public interface ProductActiveService {
    
    /**
     * Método para retornar todos los productos activos.
     * @return Retorno de todos los productos activos
     */
    Flux<ProductActive> findProducts();
    
    /**
     * Método para retornar un producto activos por su Id.
     * @param id Id del producto activo a retornar
     * @return Retorna un producto especifico
     */
    Mono<ProductActive> findById(String id);

    /**
     * Método para retornar todos los productos activos por el id del cliente.
     * @param clientId Id del cliente del cual retornar sus productos activos
     * @return Retorna los productos activos que tiene un cliente
     */
    Flux<ProductActive> findByClientId(String clientId);

    /**
     * Método para crear un nuevo producto activo.
     * @param productActive Cuerpo del producto activo a crear
     * @return Retorna el producto creado
     */
    Mono<ProductActive> createProduct(ProductActive productActive);

    /**
     * Método para actualizar un producto activo por su Id.
     * @param id Id del producto activo a actualizar
     * @param productActive Cuerpo del producto activo a actualizar
     * @return Retorno del producto activo actualizado
     */
    Mono<ProductActive> updateProduct(String id, ProductActive productActive);

    /**
     * Método para eliminar un producto activo por su Id.
     * @param id Id del producto a eliminar
     * @return Retorna un Void de eliminacion del producto
     */
    Mono<Void> deleteProduct(String id);

    /**
     * Método para realizar un consumo de credito de un producto activo por su id.
     * @param id Id del producto del cual se va a consumir su credito
     * @param consumeAmount Monto a consumir del producto
     * @return Retorno del credito actual luego del consumo
     */
    Mono<Double> consumeCredit(String id, Double consumeAmount);

    /**
     * Método para realizar el pago de crédito de un producto activo por su id.
     * @param id Id del producto del cual se va a pagar su credito
     * @param payAmount Monto a pagar del producto
     * @return Retorno del credito actual luego del consumo
     */
    Mono<Double> payCredit(String id, Double payAmount);

    Mono<Double> transfer(String originId, Double transferAmount, String finalProductId);

    Mono<CommissionReportResponse> commissionReport(String productId);
}
