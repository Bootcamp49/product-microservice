package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
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
     * @param isFromDebitCard Flag para saber si el movimiento fue usando la tarjeta
     * @return Retorno del monto actual del producto luego del debito
     */
    Mono<Double> debitMovement(String id, Double debitAmount, Boolean isFromDebitCard);

    /**
     * Método para realizar un deposito a un producto pasivo.
     * @param id Id del producto pasivo al cual se le va a realizar el deposito
     * @param depositAmount Monto del deposito al producto pasivo
     * @return Retorno del monto actual del producto luego del deposito
     */
    Mono<Double> depositMovement(String id, Double depositAmount);
    
    /**
     * Método para realizar una transferencia entre productos de debito.
     * @param originId Id del producto de origen de la transferencia
     * @param transferAmount Monto a transferir
     * @param finalId Id del producto de destino de la transferencia
     * @return Retorno del monto actual del producto de origen.
     */
    Mono<Double> transfer(String originId, Double transferAmount, String finalId);

    /**
     * Método para obtener el reporte de comisiones de un producto.
     * @param productId Id del producto del cual obtener el reporte
     * @return Retorna el reporte de comisiones del producto elegido
     */
    Mono<CommissionReportResponse> commissionReport(String productId);

    /**
     * Método para obtener el balance de movimientos de un producto.
     * @param productId Id del producto del cual obtener el reporte
     * @return Retorna el reporte del balance de movimientos del producto
     */
    Flux<MovementReportResponse> movementReport(String productId);

    /**
     * Método para realizar la asociación de una tarjeta de debito.
     * @param productId Id del producto pasivos al cual se le realiza la asociación
     * @param cardNumber Número de la tarjeta de debito para la asociación
     * @return Retorna el cuerpo del producto con los datos de la tarjeta asociada
     */

    Mono<ProductPasive> associateDebitCard(String productId, String cardNumber, 
        Boolean isPrincipalAccount);

    /**
     * Método para obtener el reporte de los últimos 10 movimientos de la tarjeta.
     * @param cardNumber Número de la tarjeta patra obtener los movimientos.
     * @return Retorna el reporte de los 10 últimos movimientos de la tarjeta.
     */
    Flux<Movement> reportLastMovementsDebitCard(String cardNumber);

    /**
     * Método para obtener el saldo actual de la tarjeta de debito.
     * @param cardNumber Número de la tarjeta a obtener el saldo actual
     * @return Retorna el monto actual de la tarjeta en la cuenta principal
     */
    Mono<Double> getCurrentBalance(String cardNumber);
}
