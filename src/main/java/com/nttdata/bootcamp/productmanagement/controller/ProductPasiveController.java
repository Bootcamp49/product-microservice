package com.nttdata.bootcamp.productmanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.service.ProductPasiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/product/pasive")
public class ProductPasiveController {

    @Autowired
    private ProductPasiveService productPasiveService;
    
    /**
     * @return Retorno de todos los productos
     */
    @GetMapping
    public Flux<ProductPasive> findProducts(){
        return productPasiveService.findProducts();
    }

    /**
     * @param id Id del producto específico a retornar
     * @return Retorno de un product especifico
     */
    @GetMapping("/{id}")
    public Mono<ProductPasive> findById(@PathVariable String id){
        return productPasiveService.findById(id);
    }

    /**
     * @param clientId Id del cliente del cual retornar el producto
     * @return Retorno de todos los productos asociados a un cliente
     */
    @GetMapping("/client/{clientId}")
    public Flux<ProductPasive> findProductsByClientId(@PathVariable String clientId){
        return productPasiveService.findByClientId(clientId);
    }

    /**
     * @param productToCreate Cuerpo del producto a crear
     * @return Retorno del producto creado
     */
    @PostMapping()
    public Mono<ProductPasive> createProduct(@RequestBody ProductPasive productToCreate){
        return productPasiveService.createProduct(productToCreate);
    }

    /**
     * @param id Id del producto a actualizar
     * @param productToUpdate Cuerpo del producto a actualizar
     * @return Retorno del producto actualizado
     */
    @PutMapping("/{id}")
    public Mono<ProductPasive> updateProduct(@PathVariable String id, @RequestBody ProductPasive productToUpdate) {
        return productPasiveService.updateProduct(id, productToUpdate);
    }

    /**
     * @param id Id del producto a eliminar 
     * @return Retorno del producto elimiado
     */
    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id){
        return productPasiveService.deleteProduct(id);
    }

    @PostMapping("/debit/{id}")
    public Mono<ProductPasive> debitMovement(@PathVariable String id, @RequestBody Double debitAmount) {
        return productPasiveService.debitMovement(id, debitAmount);
    }

    @PostMapping("/deposit/{id}")
    public Mono<ProductPasive> depositMovement(@PathVariable String id, @RequestBody Double depositAmount) {
        return productPasiveService.depositMovement(id, depositAmount);
    }
    

}
