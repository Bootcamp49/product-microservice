package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
import com.nttdata.bootcamp.productmanagement.model.MovementType;
import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.proxy.MovementProxy;
import com.nttdata.bootcamp.productmanagement.repository.ProductActiveRepository;
import java.time.LocalDate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase que implementa la interfaz de productos activos.
 */
@RequiredArgsConstructor
@Service
public class ProductActiveServiceImpl implements ProductActiveService {

    private Integer maxMovements = 20;

    private Double commissionAmount = 5.0;

    @Autowired
    private final MovementProxy movementProxy;

    @Autowired
    private final ProductActiveRepository activeRepository;

    @Autowired
    private final AdditionalValidationService additionalValidationService;

    @Override
    public Flux<ProductActive> findProducts() {
        return activeRepository.findAll();
    }

    @Override
    public Mono<ProductActive> findById(@NonNull String id) {
        return activeRepository.findById(id);
    }

    @Override
    public Flux<ProductActive> findByClientId(String clientId) {
        return activeRepository.findByClientId(clientId);
    }

    @Override
    public Mono<ProductActive> createProduct(@NonNull ProductActive productActive) {
        if (additionalValidationService.clientHasDebts(productActive.getClientId())) {
            return null;
        }
        productActive.setCreditLine(
                productActive.getCreditLine() >= 0.0 ? productActive.getCreditLine() : 0.0);
        productActive.setCurrentCredit(productActive.getCreditLine());
        productActive.setPaymentDate(LocalDate.now());
        return activeRepository.save(productActive);
    }

    @Override
    public Mono<ProductActive> updateProduct(@NonNull String id, ProductActive productActive) {
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
    public Mono<Void> deleteProduct(@NonNull String id) {
        return activeRepository.deleteById(id);
    }

    @Override
    public Mono<Double> consumeCredit(@NonNull String id, Double consumedCredit) {
        Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(3);
        movementType.setDescription("Consumo credito");

        return activeRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setCurrentCredit(existingProduct.getCurrentCredit() 
                        - consumedCredit
                        + (existingProduct.getMovements() > maxMovements ? commissionAmount : 0.0));
                    existingProduct.setMovements(existingProduct.getMovements() + 1);
                    existingProduct.setPaymentAmount(
                        existingProduct.getPaymentAmount() + consumedCredit
                    );
                    
                    movementToCreate.setClientId(existingProduct.getClientId());
                    movementToCreate.setAmountMoved(consumedCredit);
                    movementToCreate.setHasCommission(
                            existingProduct.getMovements() > maxMovements);
                    movementToCreate.setProductId(id);
                    
                    movementToCreate.setType(movementType);
                    movementProxy.createMovement(movementToCreate);
                    
                    return activeRepository.save(existingProduct);
                })
                .map(ProductActive::getCurrentCredit);
    }

    @Override
    public Mono<Double> payCredit(@NonNull String id, Double depositAmount) {
        Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(4);
        movementType.setDescription("Pago credito");

        return activeRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setCurrentCredit(existingProduct.getCurrentCredit() 
                        + depositAmount
                        - (existingProduct.getMovements() > maxMovements ? maxMovements : 0.0));
                    existingProduct.setMovements(existingProduct.getMovements() + 1);
                    existingProduct.setPaymentAmount(
                        existingProduct.getPaymentAmount() - depositAmount <= 0 ? 0 : 
                        existingProduct.getPaymentAmount() - depositAmount
                    );
                    
                    movementToCreate.setClientId(existingProduct.getClientId());
                    movementToCreate.setAmountMoved(depositAmount);
                    movementToCreate.setHasCommission(
                            existingProduct.getMovements() > maxMovements);
                    movementToCreate.setProductId(id);

                    movementToCreate.setType(movementType);
                    movementProxy.createMovement(movementToCreate);
                    return activeRepository.save(existingProduct);
                })
                .map(ProductActive::getCurrentCredit);
    }

    @Override
    public Mono<Double> transfer(String originId, 
        Double transferAmount, @NonNull String finalProductId) {
        final Mono<Double> originCurrentAmount = consumeCredit(originId, transferAmount);
        Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(5);
        movementType.setDescription("Transferencia");

        activeRepository.findById(finalProductId)
                .flatMap(existingProduct -> {
                    existingProduct.setCurrentCredit(
                        existingProduct.getCurrentCredit() + transferAmount
                    );
                    movementToCreate.setClientId(existingProduct.getClientId());
                    movementToCreate.setAmountMoved(transferAmount);
                    movementToCreate.setProductId(finalProductId);
                    movementToCreate.setType(movementType);
                    movementToCreate.setProductOriginId(originId);
                    movementProxy.createMovement(movementToCreate);

                    return activeRepository.save(existingProduct);
                }).map(ProductActive::getCurrentCredit);
        return originCurrentAmount;
    }

    @Override
    public Mono<CommissionReportResponse> commissionReport(String productId) {
        Flux<Movement> reportResponse = movementProxy.reportCommission(productId, 2);
        Integer commissionMovements = reportResponse.count().block().intValue();
        CommissionReportResponse commissionResponse = new CommissionReportResponse();
        commissionResponse.setTimesAddedCommission(commissionMovements);
        commissionResponse.setTotalCommissionAmount(commissionAmount * commissionMovements);
        return Mono.just(commissionResponse);
    }

    @Override
    public Flux<MovementReportResponse> movementReport(String productId) {
        Flux<Movement> reportResponse = movementProxy.reportMovements(productId, 2);
        MovementReportResponse movementResponse = new MovementReportResponse();
        Flux<MovementReportResponse> reportToReturn = reportResponse
            .groupBy(m -> m.getMovementDate().toLocalDate())
            .flatMap(grouped -> grouped
                .map(movementGrouped -> movementGrouped.getAmountMoved())
                .reduce(0.0, (a, b) -> {
                    return a + b;
                })
                .map(totalAmountMovemed -> {
                    movementResponse.setDay(grouped.key());
                    movementResponse.setDayAmount(totalAmountMovemed);
                    return movementResponse;
                }));
        return reportToReturn;
    }
}
