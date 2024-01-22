package com.nttdata.bootcamp.productmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.repository.ProductActiveRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ProductActiveServiceImpl implements ProductActiveService{

    @Autowired
    private final ProductActiveRepository activeRepository;
    
    @Override
    public Flux<ProductActive> findProducts() {
        return activeRepository.findAll();
    }

    @Override
    public Mono<ProductActive> findById(String id) {
        return activeRepository.findById(id);
    }

    @Override
    public Flux<ProductActive> findByClientId(String clientId) {
        return activeRepository.findByClientId(clientId);
    }

    @Override
    public Mono<ProductActive> createProduct(ProductActive productActive) {
        return activeRepository.save(productActive);
    }

    @Override
    public Mono<ProductActive> updateProduct(String id, ProductActive productActive) {
        return activeRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setAccountNumber(productActive.getAccountNumber());
            existingProduct.setCreditCardNumber(productActive.getCreditCardNumber());
            existingProduct.setCreditLine(productActive.getCreditLine());
            existingProduct.setType(productActive.getType());
            return activeRepository.save(existingProduct);
        });
    }

    @Override
    public Mono<Void> deleteProduct(String id) {
        return activeRepository.deleteById(id);
    }

    @Override
    public Mono<Double> consumeCredit(String id, Double debitAmount) {
        return activeRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setCurrentCredit(existingProduct.getCurrentCredit() - debitAmount);
            return activeRepository.save(existingProduct);
        })
        .map(ProductActive::getCurrentCredit);
    }

    @Override
    public Mono<Double> payCredit(String id, Double depositAmount) {
        return activeRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setCurrentCredit(existingProduct.getCurrentCredit() + depositAmount);
            return activeRepository.save(existingProduct);
        })
        .map(ProductActive::getCurrentCredit);
    }
    
}
