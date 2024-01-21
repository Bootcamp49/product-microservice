package com.nttdata.bootcamp.productmanagement.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/product/active")
public class ProductActiveController {
    
    /**
     * @return Retorno de todos los productos
     */
    @GetMapping()
    public Flux<ProductActive> findProducts() {
        return null;
    }

    /**
     * @return Retorno de todos los productos asociados a un cliente
     */
    @GetMapping("/user/{userId}")
    public Flux<ProductActive> findProductsByUserId(@PathVariable String param) {
        return null;
    }
    
    /**
     * @param productToCreate Cuerpo del producto a crear
     * @return Retorno del producto creado
     */
    @PostMapping()
    public Mono<ProductActive> createProduct(@RequestBody ProductActive productToCreate){
        return null;
    }

    /**
     * @param id Id del producto a actualizar
     * @param productToUpdate Cuerpo del producto a actualizar
     * @return Retorno del producto actualizado
     */
    @PutMapping("/{id}")
    public Mono<ProductActive> updateProduct(@PathVariable String id, @RequestBody ProductActive productToUpdate) {
        return null;
    }
    
    /**
     * @param id Id del producto a eliminar
     * @return Retorno del producto eliminado
     */
    @DeleteMapping("/{id}")
    public Mono<ProductActive> deleteProduct(@PathVariable String id){
        return null;
    }
}
