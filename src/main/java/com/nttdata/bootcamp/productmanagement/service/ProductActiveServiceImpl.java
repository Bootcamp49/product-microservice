package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
import com.nttdata.bootcamp.productmanagement.model.MovementType;
import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.proxy.MovementProxy;
import com.nttdata.bootcamp.productmanagement.repository.ProductActiveRepository;
import java.time.LocalDate;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
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
    @CircuitBreaker(name = "product", fallbackMethod = "multipleProductActiveFallback")
    public Flux<ProductActive> findProducts() {
        return activeRepository.findAll();
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "singleProductActiveFallback")
    public Mono<ProductActive> findById(@NonNull String id) {
        return activeRepository.findById(id);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "multipleProductActiveFallback")
    public Flux<ProductActive> findByClientId(String clientId) {
        return activeRepository.findByClientId(clientId);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "singleProductActiveFallback")
    public Mono<ProductActive> createProduct(@NonNull ProductActive productActive) {
        if (additionalValidationService.clientHasDebts(productActive.getClientId())) {
            return null;
        }
        productActive.setCreditLine(
                productActive.getCreditLine() >= 0.0 ? productActive.getCreditLine() : 0.0);
        productActive.setCurrentCredit(productActive.getCreditLine());
        productActive.setPaymentDate(LocalDate.now());
        productActive.setPaymentAmount(0.0);
        return activeRepository.save(productActive);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "singleProductActiveFallback")
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
    @CircuitBreaker(name = "product", fallbackMethod = "doubleResponses")
    public Mono<Double> consumeCredit(@NonNull String id, Double consumedCredit) { 
        MovementType movementType = new MovementType();
        movementType.setId(3);
        movementType.setDescription("Consumo credito");

        ProductActive existingProduct = activeRepository.findById(id).block();

        Movement movementToCreate = new Movement();

        movementToCreate.setClientId(existingProduct.getClientId());
        movementToCreate.setAmountMoved(-(consumedCredit));
        movementToCreate.setHasCommission(
                existingProduct.getMovements() > maxMovements);
        movementToCreate.setProductId(id);
        movementToCreate.setType(movementType);
        movementProxy.createMovement(movementToCreate).block();

        existingProduct.setCurrentCredit(existingProduct.getCurrentCredit() 
            - (consumedCredit
            + (existingProduct.getMovements() > maxMovements ? commissionAmount : 0.0)));
        existingProduct.setMovements(existingProduct.getMovements() + 1);
        existingProduct.setPaymentAmount(
            existingProduct.getPaymentAmount() + consumedCredit);

        return activeRepository.save(existingProduct).map(ProductActive::getCurrentCredit);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "doubleResponses")
    public Mono<Double> payCredit(@NonNull String id, Double depositAmount, 
        String pasiveProductId) {
        MovementType movementType = new MovementType();
        movementType.setId(4);
        movementType.setDescription("Pago credito");
        Movement movementToCreate = new Movement();
        if (pasiveProductId != null) {
            if (!additionalValidationService
                .productPasiveValidToPay(pasiveProductId, depositAmount)) {
                pasiveProductId = additionalValidationService
                    .productToMakeDebitPay(pasiveProductId, depositAmount) != null 
                    ? additionalValidationService.productToMakeDebitPay(pasiveProductId, 
                        depositAmount).getId() : null;
                if (pasiveProductId == null) {
                    return null;
                }
            }
            movementToCreate.setProductOriginId(pasiveProductId);
            movementToCreate.setIsFromDebitCard(true);
        }

        ProductActive existingProduct = activeRepository.findById(id).block();

        movementToCreate.setClientId(existingProduct.getClientId());
        movementToCreate.setAmountMoved(depositAmount);
        movementToCreate.setHasCommission(
                existingProduct.getMovements() > maxMovements);
        movementToCreate.setProductId(id);
        movementToCreate.setType(movementType);
        movementProxy.createMovement(movementToCreate).block();

        existingProduct.setCurrentCredit(existingProduct.getCurrentCredit() 
            + (depositAmount
            - (existingProduct.getMovements() > maxMovements ? maxMovements : 0.0)));
        existingProduct.setMovements(existingProduct.getMovements() + 1);
        existingProduct.setPaymentAmount(
            existingProduct.getPaymentAmount() - depositAmount <= 0 ? 0 : 
            existingProduct.getPaymentAmount() - depositAmount
        );
        return activeRepository.save(existingProduct)
        .map(ProductActive::getCurrentCredit);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "doubleResponses")
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
    @CircuitBreaker(name = "product", fallbackMethod = "commissionReportFallback")
    public Mono<CommissionReportResponse> commissionReport(String productId) {
        Flux<Movement> reportResponse = movementProxy.reportCommission(productId, 2);
        Integer commissionMovements = reportResponse.count().block().intValue();
        CommissionReportResponse commissionResponse = new CommissionReportResponse();
        commissionResponse.setTimesAddedCommission(commissionMovements);
        commissionResponse.setTotalCommissionAmount(commissionAmount * commissionMovements);
        return Mono.just(commissionResponse);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "movementReportFallback")
    public Flux<MovementReportResponse> movementReport(String productId) {
        Flux<Movement> reportResponse = movementProxy.reportMovements(productId, 2);

        Flux<MovementReportResponse> reportToReturn = reportResponse.groupBy(m -> m.getMovementDate().toLocalDate())
                .flatMap(grouped -> grouped
                        .map(movementGrouped -> movementGrouped.getAmountMoved())
                        .reduce((a,b) -> {
                            return a+b;
                        }).map(totalAmountMoved -> {
                            MovementReportResponse movementResponse = new MovementReportResponse();
                            movementResponse.setDay(grouped.key());
                            movementResponse.setDayAmount(totalAmountMoved);
                            return movementResponse;
                        }));
        return reportToReturn;
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "movementFallback")
    public Flux<Movement> reportLastMovementsCreditCard(String cardNumber) {
        List<String> productId = activeRepository
            .findByCreditCardNumber(cardNumber).map(p -> p.getId()).collectList().block();

        if (productId.size() <= 0) {
            return null;
        }
        Flux<Movement> lastMovements = movementProxy.getMovementReportByCard(String.join(",", productId), 2);
        return lastMovements;
    }

    private Mono<ProductActive> singleProductActiveFallback(Throwable throwable){
        ProductActive productActiveToReturn = new ProductActive();
        return Mono.just(productActiveToReturn);
    }
    private Flux<ProductActive> multipleProductActiveFallback(Throwable throwable){
        ProductActive productActive = new ProductActive();
        return Flux.just(productActive);
    }
    private Mono<Double> doubleResponses(Throwable throwable){
        System.out.println("Dentro del fallback");
        System.out.println("Throwable: " + throwable.getMessage());
        return Mono.just(0.0);
    }
    private Mono<CommissionReportResponse> commissionReportFallback(Throwable throwable){
        CommissionReportResponse commissionReport = new CommissionReportResponse();
        return Mono.just(commissionReport);
    }
    private Flux<MovementReportResponse> movementReportFallback(Throwable throwable){
        MovementReportResponse movementReport = new MovementReportResponse();
        return Flux.just(movementReport);
    }
    private Flux<Movement> movementFallback(Throwable throwable){
        System.out.println("Dentro del fallback");
        System.out.println("Throwable: " + throwable);
        Movement movement = new Movement();
        return Flux.just(movement);
    }
}
