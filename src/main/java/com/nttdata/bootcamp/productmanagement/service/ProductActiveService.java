package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
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
     * @param debitCardNumber Númer de tarjeta de debito con la que se está haciendo el pago
     * @return Retorno del credito actual luego del consumo
     */
    Mono<Double> payCredit(String id, Double payAmount, String debitCardNumber);

    /**
     * Método para realizar transferencias entre cuentas de crédito.
     * @param originId Id del producto desde donde se está haciendo la transferencia
     * @param transferAmount Monto a transferir
     * @param finalProductId Id del producto de destino de la transferencia
     * @return Retorno del saldo actual luego de la transferencia del producto de origen
     */
    Mono<Double> transfer(String originId, Double transferAmount, String finalProductId);

    /**
     * Método para retornar el reporte de comisiones.
     * @param productId Id del producto del cual obtener el reporte de comisiones
     * @return Retorna el reporte con la cantidad de movimientos con comisión y el total
     */
    Mono<CommissionReportResponse> commissionReport(String productId);

    /**
     * Método para retornar el reporte de movimientos.
     * @param productId Id del producto a retornar el reporte
     * @return Retorna el balance diario de movimientos diarios del mes actual
     */
    Flux<MovementReportResponse> movementReport(String productId);

    /**
     * Método para retornar el reporte de ultimos 10 movimientos de la tarjeta.
     * @param cardNumber Número de la tarjeta del cual obtener los movimientos
     * @return Retorna el reporte de los últimos 10 movimientos de la tarjeta de Credito
     */
    Flux<Movement> reportLastMovementsCreditCard(String cardNumber);
}
