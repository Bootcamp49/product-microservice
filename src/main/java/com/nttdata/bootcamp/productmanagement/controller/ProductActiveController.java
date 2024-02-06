package com.nttdata.bootcamp.productmanagement.controller;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.model.TransferRequest;
import com.nttdata.bootcamp.productmanagement.service.ProductActiveService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Controlador para los productos activos.
 */
@RestController
@RequestMapping("/product/active")
public class ProductActiveController {

    @Autowired
    private ProductActiveService productActiveService;

    /**
     * Método para el retorno de todos los productos activos.
     * 
     * @return Retorno de todos los productos activos
     */
    @GetMapping()
    public Flux<ProductActive> findProducts() {
        return productActiveService.findProducts();
    }

    /**
     * Método para el retorno de un producto activo por su id.
     * 
     * @param id Id del producto específico a retornar
     * @return Retorno de un producto específico
     */
    @GetMapping("/{id}")
    public Mono<ProductActive> findById(@PathVariable String id) {
        return productActiveService.findById(id);
    }

    /**
     * Método para el retorno de productos activos por el id de un cliente.
     * 
     * @param clientId Id del cliente del cual retornar el producto
     * @return Retorno de todos los productos asociados a un cliente
     */
    @GetMapping("/client/{clientId}")
    public Flux<ProductActive> findProductsByClientId(@PathVariable String clientId) {
        return productActiveService.findByClientId(clientId);
    }

    /**
     * Método para crear un nuevo producto activo.
     * 
     * @param productToCreate Cuerpo del producto a crear
     * @return Retorno del producto creado
     */
    @PostMapping()
    public Mono<ProductActive> createProduct(@RequestBody ProductActive productToCreate) {
        return productActiveService.createProduct(productToCreate);
    }

    /**
     * Método para actualizar un producto activo según su Id.
     * 
     * @param id              Id del producto a actualizar
     * @param productToUpdate Cuerpo del producto a actualizar
     * @return Retorno del producto actualizado
     */
    @PutMapping("/{id}")
    public Mono<ProductActive> updateProduct(
            @PathVariable String id,
            @RequestBody ProductActive productToUpdate) {
        return productActiveService.updateProduct(id, productToUpdate);
    }

    /**
     * Método para la eliminación de un producto activo por su Id.
     * 
     * @param id Id del producto a eliminar
     * @return Retorno del producto eliminado
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productActiveService.deleteProduct(id);
    }

    /**
     * Método para realizar un consumo de crédito de un producto activo por su Id.
     * 
     * @param id            Id del producto al cual se le va a sustraer credito
     * @param consumeAmount Monto a sustraer del credito
     * @return Retorno del monto actual luego del consumo.
     */
    @PutMapping("/consumecredit/{id}")
    public Mono<Double> consumeCredit(@PathVariable String id, 
        @RequestBody Map<String, Double> consumeAmount) {
        return productActiveService.consumeCredit(id, consumeAmount.get("consumeAmount"));
    }

    /**
     * Método para realizar un pago de crédito de un producto activo por su Id.
     * 
     * @param id        Id del producto al cual se le va a realizar el pago de
     *                  credito
     * @param requestBody Cuerpo de la solicitud con el monto para pagar y el id del productoPasivo
     * @return Retorno del credito actual luego del pago
     */
    @PutMapping("/paycredit/{id}")
    public Mono<Double> payCredit(@PathVariable String id, 
        @RequestBody Map<String, Object> requestBody) {
        return productActiveService.payCredit(id, (Double) requestBody.get("payAmount"), 
        requestBody.get("pasiveProductId") != null 
            ? requestBody.get("pasiveProductId").toString() : null);
    }

    /**
     * Método para realizar las transferencias entre diferentes productos.
     * @param originId El id del producto de origen de la transferencia
     * @param transferRequest El cuerpo de la transferencia con el monto y el Id de destino
     * @return Retorna el monto actual de la cuenta de origen.
     */
    @PutMapping("/transfer/{originId}")
    public Mono<Double> transfer(@PathVariable String originId,
            @RequestBody TransferRequest transferRequest) {
        return productActiveService.transfer(originId, 
        transferRequest.getTransferAmount(), 
        transferRequest.getFinalProductId());
    }

    /**
     * Método para obtener un reporte de los movimientos que tienen comisióon.
     * @param productId Id del producto del cual obtener el reporte
     * @return Retorna el la cantidad y la suma total de las comisiones cobradas hasta la fecha.
     */
    @GetMapping("/report/commission/{productId}")
    public Mono<CommissionReportResponse> commissionReport(@PathVariable String productId) {
        return productActiveService.commissionReport(productId);
    }

    /**
     * Método para obtener el reporte de movimientos de productos activos.
     * @param productId Id del producto activo a obtener el reporte
     * @return devuelve un balance diarío del mes actual sobre los movimientos del producto
     */
    @GetMapping("/report/movements/{productId}")
    public Flux<MovementReportResponse> movementReport(@PathVariable String productId) {
        return productActiveService.movementReport(productId);
    }
    
    /**
     * Método para obtener el reporte de los últimos 10 movimientos de la tarjeta de credito.
     * @param cardNumber Número de la tarjeta de credito del cual obtener el reporte
     * @return devuelve los últimos 10 movimientos de la tarjeta de credito
     */
    @GetMapping("/report/creditcard/movements/{cardNumber}")
    public Flux<Movement> reportLastMovementsCreditCard(@PathVariable String cardNumber) {
        return productActiveService.reportLastMovementsCreditCard(cardNumber);
    }
}
