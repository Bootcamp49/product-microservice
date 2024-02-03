package com.nttdata.bootcamp.productmanagement.controller;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.model.TransferRequest;
import com.nttdata.bootcamp.productmanagement.service.ProductPasiveService;
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
@RequestMapping("/product/pasive")
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
     * @param debitAmount Monto a debitar del producto
     * @return Retorno del monto actual luego del debito
     */
    @PutMapping("/debit/{id}")
    public Mono<Double> debitMovement(@PathVariable String id, @RequestBody Double debitAmount) {
        return productPasiveService.debitMovement(id, debitAmount);
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
            @RequestBody Double depositAmount) {
        return productPasiveService.depositMovement(id, depositAmount);
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
     * @return Retorna el la cantidad y la suma total de las comisiones cobradas hasta la fecha.
     */
    @GetMapping("/report/commission/{productId}")
    public Mono<CommissionReportResponse> commissionReport(@PathVariable String productId) {
        return productPasiveService.commissionReport(productId);
    }

    @GetMapping("/report/movements/{productId}")
    public Flux<MovementReportResponse> movementReport(@PathVariable String productId) {
        return productPasiveService.movementReport(productId);
    }
    

}
