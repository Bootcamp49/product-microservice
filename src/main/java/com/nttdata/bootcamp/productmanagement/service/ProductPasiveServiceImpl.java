package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementType;
import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.proxy.MovementProxy;
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

    @Autowired
    private final MovementProxy movementProxy;

    private Integer maxMovements = 20;

    private Double commissionAmount = 5.0;

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
        productPasive.setCurrentAmount(
                productPasive.getCurrentAmount() >= 0.0 ? productPasive.getCurrentAmount() : 0.0);
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
        Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(1);
        movementType.setDescription("Debito");

        Mono<Double> responseDebitMovement = pasiveRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setCurrentAmount(
                        existingProduct.getCurrentAmount() - debitAmount
                        + (existingProduct.getMovements() > maxMovements ? commissionAmount : 0.0));
                    existingProduct.setMovements(existingProduct.getMovements() + 1);

                    movementToCreate.setClientId(existingProduct.getClientId());
                    movementToCreate.setAmountMoved(debitAmount);
                    movementToCreate.setHasCommission(
                            existingProduct.getMovements() > maxMovements);
                    movementToCreate.setProductId(id);

                    movementToCreate.setType(movementType);
                    movementProxy.createMovement(movementToCreate);

                    return pasiveRepository.save(existingProduct);
                })
                .map(ProductPasive::getCurrentAmount);
        return responseDebitMovement;
    }

    @Override
    public Mono<Double> depositMovement(@NonNull String id, Double depositAmount) {
        Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(2);
        movementType.setDescription("Deposito");

        return pasiveRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setCurrentAmount(existingProduct.getCurrentAmount() 
                        + depositAmount
                        - (existingProduct.getMovements() > maxMovements ? commissionAmount : 0.0));
                    existingProduct.setMovements(existingProduct.getMovements() + 1);

                    movementToCreate.setClientId(existingProduct.getClientId());
                    movementToCreate.setAmountMoved(depositAmount);
                    movementToCreate.setHasCommission(
                            existingProduct.getMovements() > maxMovements);
                    movementToCreate.setProductId(id);

                    movementToCreate.setType(movementType);
                    movementProxy.createMovement(movementToCreate);
                    return pasiveRepository.save(existingProduct);
                })
                .map(ProductPasive::getCurrentAmount);
    }

    @Override
    public Mono<Double> transfer(@NonNull String originId,
            Double transferAmount, @NonNull String finalId) {
        
        final Mono<Double> originCurrentAmount = depositMovement(originId, transferAmount);
        final Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(5);
        movementType.setDescription("Transferencia");

        pasiveRepository.findById(finalId)
                .flatMap(existingProduct -> {
                    existingProduct.setCurrentAmount(
                        existingProduct.getCurrentAmount() + transferAmount
                    );
                    movementToCreate.setClientId(existingProduct.getClientId());
                    movementToCreate.setAmountMoved(transferAmount);
                    movementToCreate.setProductId(finalId);
                    movementToCreate.setType(movementType);
                    movementToCreate.setProductOriginId(originId);
                    movementProxy.createMovement(movementToCreate);

                    return pasiveRepository.save(existingProduct);
                }).map(ProductPasive::getCurrentAmount);

        return originCurrentAmount;
    }
    
    @Override
    public Mono<CommissionReportResponse> commissionReport(String productId) {
        Flux<Movement> reportResponse = movementProxy.reportCommission(productId, 1);
        Integer commissionMovements = reportResponse.count().block().intValue();
        CommissionReportResponse commissionResponse = new CommissionReportResponse();
        commissionResponse.setTimesAddedCommission(commissionMovements);
        commissionResponse.setTotalCommissionAmount(commissionAmount * commissionMovements);
        return Mono.just(commissionResponse);
    }
}
