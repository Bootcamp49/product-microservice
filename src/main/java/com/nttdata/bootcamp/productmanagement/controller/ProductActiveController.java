package com.nttdata.bootcamp.productmanagement.controller;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.service.ProductActiveService;
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
    public Mono<Double> consumeCredit(@PathVariable String id, @RequestBody Double consumeAmount) {
        return productActiveService.consumeCredit(id, consumeAmount);
    }

    /**
     * Método para realizar un pago de crédito de un producto activo por su Id.
     * 
     * @param id        Id del producto al cual se le va a realizar el pago de
     *                  credito
     * @param payAmount Monto a pagar del credito
     * @return Retorno del credito actual luego del pago
     */
    @PutMapping("/paycredit/{id}")
    public Mono<Double> payCredit(@PathVariable String id, @RequestBody Double payAmount) {
        return productActiveService.payCredit(id, payAmount);
    }
}
