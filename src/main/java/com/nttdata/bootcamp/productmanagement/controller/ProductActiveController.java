package com.nttdata.bootcamp.productmanagement.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.service.ProductActiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/product/active")
public class ProductActiveController {

    @Autowired
    private ProductActiveService productActiveService;
    
    /**
     * @return Retorno de todos los productos
     */
    @GetMapping()
    public Flux<ProductActive> findProducts() {
        return productActiveService.findProducts();
    }

    /**
     * @param id Id del producto específico a retornar
     * @return Retorno de un producto específico
     */
    @GetMapping("/{id}")
    public Mono<ProductActive> findById(@PathVariable String id) {
        return productActiveService.findById(id);
    }
    
    /**
     * @param clientId Id del cliente del cual retornar el producto
     * @return Retorno de todos los productos asociados a un cliente
     */
    @GetMapping("/client/{clientId}")
    public Flux<ProductActive> findProductsByClientId(@PathVariable String clientId) {
        return productActiveService.findByClientId(clientId);
    }
    
    /**
     * @param productToCreate Cuerpo del producto a crear
     * @return Retorno del producto creado
     */
    @PostMapping()
    public Mono<ProductActive> createProduct(@RequestBody ProductActive productToCreate){
        return productActiveService.createProduct(productToCreate);
    }

    /**
     * @param id Id del producto a actualizar
     * @param productToUpdate Cuerpo del producto a actualizar
     * @return Retorno del producto actualizado
     */
    @PutMapping("/{id}")
    public Mono<ProductActive> updateProduct(@PathVariable String id, @RequestBody ProductActive productToUpdate) {
        return productActiveService.updateProduct(id, productToUpdate);
    }
    
    /**
     * @param id Id del producto a eliminar
     * @return Retorno del producto eliminado
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return productActiveService.deleteProduct(id);
    }

    @PostMapping("/debit/{id}")
    public Mono<ProductActive> debitMovement(@PathVariable String id, @RequestBody Double debitAmount){
        return productActiveService.debitMovement(id, debitAmount);
    }
    @PostMapping("/deposit/{id}")
    public Mono<ProductActive> depositMovement(@PathVariable String id, @RequestBody Double depositAmount){
        return productActiveService.depositMovement(id, depositAmount);
    }
}
