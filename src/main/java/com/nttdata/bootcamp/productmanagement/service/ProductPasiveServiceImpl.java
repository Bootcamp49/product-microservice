package com.nttdata.bootcamp.productmanagement.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.model.ProductPasiveType;
import com.nttdata.bootcamp.productmanagement.repository.ProductPasiveRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ProductPasiveServiceImpl implements ProductPasiveService{

    @Autowired
    private final ProductPasiveRepository pasiveRepository;
    
    @Override
    public Flux<ProductPasive> findProducts() {
        return pasiveRepository.findAll();
    }

    @Override
    public Flux<ProductPasive> findByClientId(String clientId) {
        return pasiveRepository.findByClientId(clientId);
    }

    @Override
    public Mono<ProductPasive> createProduct(ProductPasive productPasive) {
        productPasive.setCreationDate(LocalDate.now());
        productPasive.setMovements(0);
        return pasiveRepository.save(productPasive);
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return pasiveRepository.deleteById(id);
    }

    @Override
    public Mono<ProductPasive> updateProduct(String id, ProductPasive product) {
        return pasiveRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setAccountNumber(product.getAccountNumber());
            existingProduct.setCurrentAmount(product.getCurrentAmount());
            existingProduct.setMovements(product.getMovements());
            existingProduct.setType(product.getType());
            return pasiveRepository.save(existingProduct);
        });
    }

    @Override
    public Mono<ProductPasive> findById(String id) {
        return pasiveRepository.findById(id);
    }

    @Override
    public Mono<ProductPasive> debitMovement(String id, ProductPasive product) {
        return pasiveRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setCurrentAmount(existingProduct.getCurrentAmount() - product.getCurrentAmount());
            existingProduct.setMovements(product.getMovements());
            return pasiveRepository.save(existingProduct);
        });
    }

    @Override
    public Mono<ProductPasive> depositMovement(String id, ProductPasive product) {
        return pasiveRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setCurrentAmount(existingProduct.getCurrentAmount() + product.getCurrentAmount());
            existingProduct.setMovements(product.getMovements());
            return pasiveRepository.save(existingProduct);
        });
    }

    
}
