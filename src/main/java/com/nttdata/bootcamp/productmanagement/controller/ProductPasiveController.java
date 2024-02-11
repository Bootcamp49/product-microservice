package com.nttdata.bootcamp.productmanagement.controller;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.model.TransferRequest;
import com.nttdata.bootcamp.productmanagement.service.ProductPasiveService;
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
 * Controlador para los productos pasivos.
 */
@RestController
@RequestMapping("/pasive")
public class ProductPasiveController {

    @Autowired
    private ProductPasiveService productPasiveService;

    /**
     * Método para retornar todos los productos pasivos.
     * @return Retorno de todos los productos
     */
    @GetMapping
    public Flux<ProductPasive> findProducts() {
        return productPasiveService.findProducts();
    }

    /**
     * Método para retornar un producto pasivo específico según su Id.
     * @param id Id del producto específico a retornar
     * @return Retorno de un product especifico
     */
    @GetMapping("/{id}")
    public Mono<ProductPasive> findById(@PathVariable String id) {
        return productPasiveService.findById(id);
    }

    /**
     * Método para retornar todos los productos pasivos según el id de un cliente.
     * @param clientId Id del cliente del cual retornar el producto
     * @return Retorno de todos los productos asociados a un cliente
     */
    @GetMapping("/client/{clientId}")
    public Flux<ProductPasive> findProductsByClientId(@PathVariable String clientId) {
        return productPasiveService.findByClientId(clientId);
    }

    /**
     * Método para crear un nuevo producto pasivo.
     * @param productToCreate Cuerpo del producto a crear
     * @return Retorno del producto creado
     */
    @PostMapping()
    public Mono<ProductPasive> createProduct(@RequestBody ProductPasive productToCreate) {
        return productPasiveService.createProduct(productToCreate);
    }

    /**
     * Método para actualizar un producto pasivo según su Id.
     * @param id              Id del producto a actualizar
     * @param productToUpdate Cuerpo del producto a actualizar
     * @return Retorno del producto actualizado
     */
    @PutMapping("/{id}")
    public Mono<ProductPasive> updateProduct(
            @PathVariable String id,
            @RequestBody ProductPasive productToUpdate) {
        return productPasiveService.updateProduct(id, productToUpdate);
    }

    /**
     * Método para eliminar un producto pasivo según su Id.
     * @param id Id del producto a eliminar
     * @return Retorno del producto elimiado
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productPasiveService.deleteProduct(id);
    }

    /**
     * Método para realizar un debito a un producto pasivo según su Id.
     * @param id          Id del producto al cual se le va a realizar el debito
     * @param requestBody Cuerpo de la solicitud con el monto para debitar y un campo para saber si
     se usó la tarjeta para el movimiento.
     * @return Retorno del monto actual luego del debito
     */
    @PutMapping("/debit/{id}")
    public Mono<Double> debitMovement(@PathVariable String id, 
        @RequestBody Map<String, Object> requestBody) {
        return productPasiveService.debitMovement(id, 
        (Double) requestBody.get("debitAmount"), 
        requestBody.get("isFromDebitCard") != null
            ? (Boolean) requestBody.get("isFromDebitCard") : false);
    }

    /**
     * Método para realizar un deposito a un producto pasivo según su Id.
     * @param id            Id del producto al cual se le va a realizar el deposito
     * @param depositAmount Monto a depositar del producto
     * @return Retorno del monto actual luego del deposito
     */
    @PutMapping("/deposit/{id}")
    public Mono<Double> depositMovement(
            @PathVariable String id,
            @RequestBody Map<String, Double> depositAmount) {
        return productPasiveService.depositMovement(id, depositAmount.get("depositAmount"));
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
        
        return productPasiveService.transfer(originId, 
            transferRequest.getTransferAmount(), 
            transferRequest.getFinalProductId());
    }

    /**
     * Método para obtener un reporte de los movimientos que tienen comisióon.
     * @param productId Id del producto del cual obtener el reporte
     * @return Retorna la cantidad y la suma total de las comisiones cobradas hasta la fecha.
     */
    @GetMapping("/report/commission/{productId}")
    public Mono<CommissionReportResponse> commissionReport(@PathVariable String productId) {
        return productPasiveService.commissionReport(productId);
    }

    /**
     * Método para obtener un reporte de movimientos del mes actual.
     * @param productId Id del producto del cual obtener el reporte.
     * @return Retorna el reporte de movimientos del mes actual.
     */
    @GetMapping("/report/movements/{productId}")
    public Flux<MovementReportResponse> movementReport(@PathVariable String productId) {
        return productPasiveService.movementReport(productId);
    }

    /**
     * Método para asociar tarjetas de debito.
     * @param productId Id del producto a asociar la tarjeta
     * @param requestBody Cuerpo de la solicitud con el número de la tarjeta y un flag
     para identificar si es la cuenta principal de la tarjeta.
     * @return Retorna el cuerpo del producto al que se le asoció la tarjeta
     */
    @PutMapping("/debitcard/associate/{productId}")
    public Mono<ProductPasive> associateDebitCard(@PathVariable String productId, 
        @RequestBody Map<String, Object> requestBody) {
        return productPasiveService.associateDebitCard(productId, 
        requestBody.get("cardNumber").toString(), 
        requestBody.get("isPrincipalAccount") != null 
            ? (Boolean) requestBody.get("isPrincipalAccount") : false);
    }

    /**
     * Método para obtener el reporte de los últimos movimientos de la tarjeta pasiva.
     * @param cardNumber Número de la tarjeta de debito del cual obtener los movimientos
     * @return Retorna los últimos 10 movimientos de la tarjeta de debito
     */
    @GetMapping("/debitcard/movements/{cardNumber}")
    public Flux<Movement> reportLastMovementsDebitCard(
        @PathVariable String cardNumber) {
        return productPasiveService.reportLastMovementsDebitCard(cardNumber);
    }

    /**
     * Método para obtener el saldo actual de la tarjeta de debito.
     * @param cardNumber Número de la tarjeta de debito de la cual obtener el saldo actual
     * @return Retorna el monto actual de la tarjeta de debito
     */
    @GetMapping("/debitcard/current/{cardNumber}")
    public Mono<Double> currentDebitCardBalance(@PathVariable String cardNumber) {
        return productPasiveService.getCurrentBalance(cardNumber);
    }
    
}
