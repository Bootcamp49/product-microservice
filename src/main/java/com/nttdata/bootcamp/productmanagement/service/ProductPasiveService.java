package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz para los productos pasivos.
 */
public interface ProductPasiveService {

    /**
     * Método para retornar todos los productos pasivos.
     * @return Retorno de todos los productos pasivos
     */
    Flux<ProductPasive> findProducts();
    
    /**
     * Método para retornar un producto por su Id.
     * @param id Id del producto pasivo a retornar
     * @return Retorna un producto especifico
     */
    Mono<ProductPasive> findById(String id);
    
    /**
     * Método para retornar todos los productos pasivos por el id de un cliente.
     * @param clientId Id del cliente del cual retornar sus productos pasivos
     * @return Retornar los productos pasivos de un cliente especifico
     */
    Flux<ProductPasive> findByClientId(String clientId);

    /**
     * Método para crear un nuevo producto pasivo.
     * @param product Cuerpo del producto pasivo a crear
     * @return Retorna el producto creado
     */
    Mono<ProductPasive> createProduct(ProductPasive product);

    /**
     * Método para actualizar un producto pasivo por su Id.
     * @param id Id del producto pasivo a actualizar
     * @param productPasive Cuerpo del producto pasivo a actualizar
     * @return Retorno del producto pasivo actualizado
     */
    Mono<ProductPasive> updateProduct(String id, ProductPasive productPasive);

    /**
     * Método para eliminar un producto por su Id.
     * @param id Id del producto pasivo a eliminar
     * @return Retorna un Void de eliminacion del producto
     */
    Mono<Void> deleteProduct(String id);

    /**
     * Método para realizar un debito a un producto pasivo.
     * @param id Id del producto del cual se va a realizar un debito
     * @param debitAmount Monto a debitar del producto pasivo
     * @return Retorno del monto actual del producto luego del debito
     */
    Mono<Double> debitMovement(String id, Double debitAmount);

    /**
     * Método para realizar un deposito a un producto pasivo.
     * @param id Id del producto pasivo al cual se le va a realizar el deposito
     * @param depositAmount Monto del deposito al producto pasivo
     * @return Retorno del monto actual del producto luego del deposito
     */
    Mono<Double> depositMovement(String id, Double depositAmount);
    
    Mono<Double> transfer(String originId, Double transferAmount, String finalId);

    Mono<CommissionReportResponse> commissionReport(String productId);
}
