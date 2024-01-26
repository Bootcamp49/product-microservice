package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.repository.ProductPasiveRepository;
import java.time.LocalDate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase que implementa la interfaz de los productos pasivos.
 */
@RequiredArgsConstructor
@Service
public class ProductPasiveServiceImpl implements ProductPasiveService {

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
    public Mono<Void> deleteProduct(@NonNull String id) {
        return pasiveRepository.deleteById(id);
    }

    @Override
    public Mono<ProductPasive> updateProduct(@NonNull String id, ProductPasive product) {
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
    public Mono<ProductPasive> findById(@NonNull String id) {
        return pasiveRepository.findById(id);
    }

    @Override
    public Mono<Double> debitMovement(@NonNull String id, Double debitAmount) {
        return pasiveRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setCurrentAmount(existingProduct.getCurrentAmount() - debitAmount);
            existingProduct.setMovements(existingProduct.getMovements() + 1);
            return pasiveRepository.save(existingProduct);
        })
        .map(ProductPasive::getCurrentAmount);
    }

    @Override
    public Mono<Double> depositMovement(@NonNull String id, Double depositAmount) {
        return pasiveRepository.findById(id)
        .flatMap(existingProduct -> {
            existingProduct.setCurrentAmount(existingProduct.getCurrentAmount() + depositAmount);
            existingProduct.setMovements(existingProduct.getMovements() + 1);
            return pasiveRepository.save(existingProduct);
        })
        .map(ProductPasive::getCurrentAmount);
    }

    
}
