package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.CommissionReportResponse;
import com.nttdata.bootcamp.productmanagement.model.Movement;
import com.nttdata.bootcamp.productmanagement.model.MovementReportResponse;
import com.nttdata.bootcamp.productmanagement.model.MovementType;
import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.proxy.ClientProxy;
import com.nttdata.bootcamp.productmanagement.proxy.MovementProxy;
import com.nttdata.bootcamp.productmanagement.repository.ProductPasiveRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @Autowired
    private final ClientProxy clientProxy;

    final private Integer maxMovements = 20;

    final private Double commissionAmount = 5.0;

    final private Integer maxMonthMovements = 10;

    @Autowired
    private final AdditionalValidationService additionalValidationService;

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "multiplePasiveFallback")
    public Flux<ProductPasive> findProducts() {
        return pasiveRepository.findAll();
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "multiplePasiveFallback")
    public Flux<ProductPasive> findByClientId(String clientId) {
        return pasiveRepository.findByClientId(clientId);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "singlePasiveFallback")
    public Mono<ProductPasive> createProduct(ProductPasive productPasive) {
        if (additionalValidationService.clientHasDebts(productPasive.getClientId())) {
            return null;
        }
        String clientType = clientProxy.getClientById(productPasive.getClientId()).getClientType();
        boolean notValidToCreate;
        if(clientType.equalsIgnoreCase("Personal")){
            Flux<ProductPasive> otherPasiveProducts = pasiveRepository.findByClientId(productPasive.getClientId());
            if(productPasive.getType().getId() == 1 || productPasive.getType().getId() == 2) {
                notValidToCreate = otherPasiveProducts.count().block().intValue() > 0;
            } else {
                notValidToCreate = otherPasiveProducts.count().block().intValue() > 0 &&
                        otherPasiveProducts.filter(p -> p.getType().getId() == 1 || p.getType().getId() == 2)
                                .count().block().intValue() > 0;
            }
            if (notValidToCreate)
                return null;
        } else if(clientType.equalsIgnoreCase("Empresarial")){
            notValidToCreate = productPasive.getType().getId().equals(1) || productPasive.getType().getId().equals(3);
            if(notValidToCreate)
                return null;
        }

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
    @CircuitBreaker(name = "product", fallbackMethod = "singlePasiveFallback")
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
    @CircuitBreaker(name = "product", fallbackMethod = "singlePasiveFallback")
    public Mono<ProductPasive> findById(@NonNull String id) {
        return pasiveRepository.findById(id);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "doubleResponses")
    public Mono<Double> debitMovement(@NonNull String id, Double debitAmount, 
        Boolean isFromDebitCard) {
        MovementType movementType = new MovementType();
        movementType.setId(1);
        movementType.setDescription("Debito");
        Movement movementToCreate = new Movement();
        ProductPasive productToDebit = pasiveRepository.findById(id).block();
        if((productToDebit.getType().getId() == 1 && productToDebit.getMovements() > maxMonthMovements) ||
            productToDebit.getType().getId() == 3 && productToDebit.getMovements() >= 1)
            return null;


        if (isFromDebitCard) {
            if (productToDebit.getCurrentAmount() < debitAmount) {
                productToDebit = additionalValidationService.productToMakeDebitPay(id, debitAmount);
                if (productToDebit == null) {
                    return null;
                }
            }
        }
        movementToCreate.setClientId(productToDebit.getClientId());
        movementToCreate.setAmountMoved(-(debitAmount));
        movementToCreate.setHasCommission(
            productToDebit.getMovements() > maxMovements);
        movementToCreate.setProductId(id);
        movementToCreate.setIsFromDebitCard(isFromDebitCard);
        movementToCreate.setType(movementType);
        movementProxy.createMovement(movementToCreate);

        productToDebit.setCurrentAmount(
            productToDebit.getCurrentAmount() - (debitAmount
            + (productToDebit.getType().getId() == 3 ?
                    (productToDebit.getMovements() > maxMovements ? commissionAmount : 0.0) : 0.0)));
        productToDebit.setMovements(productToDebit.getMovements() + 1);
        
        return pasiveRepository.save(productToDebit).map(ProductPasive::getCurrentAmount);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "doubleResponses")
    public Mono<Double> depositMovement(@NonNull String id, Double depositAmount) {
        Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(2);
        movementType.setDescription("Deposito");

        ProductPasive existingProduct = pasiveRepository.findById(id).block();

        if((existingProduct.getType().getId() == 1 && existingProduct.getMovements() > maxMonthMovements) ||
            existingProduct.getType().getId() == 3 && existingProduct.getMovements() >= 1)
            return null;

        movementToCreate.setClientId(existingProduct.getClientId());
        movementToCreate.setAmountMoved(depositAmount);
        movementToCreate.setHasCommission(
                existingProduct.getMovements() > maxMovements);
        movementToCreate.setProductId(id);
        movementToCreate.setType(movementType);
        movementProxy.createMovement(movementToCreate);

        existingProduct.setCurrentAmount(existingProduct.getCurrentAmount() 
            + (depositAmount
            - (existingProduct.getType().getId() == 3 ?
                (existingProduct.getMovements() > maxMovements ? commissionAmount : 0.0) : 0.0)));
        existingProduct.setMovements(existingProduct.getMovements() + 1);
        return pasiveRepository.save(existingProduct)
                .map(ProductPasive::getCurrentAmount);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "doubleResponses")
    public Mono<Double> transfer(@NonNull String originId,
            Double transferAmount, @NonNull String finalId) {
        
        final Mono<Double> originCurrentAmount = debitMovement(originId, transferAmount, false);
        if(originCurrentAmount == null){
            return null;
        }
        final Movement movementToCreate = new Movement();
        MovementType movementType = new MovementType();
        movementType.setId(5);
        movementType.setDescription("Transferencia");
        ProductPasive existingProduct = pasiveRepository.findById(finalId).block();

        movementToCreate.setClientId(existingProduct.getClientId());
        movementToCreate.setAmountMoved(transferAmount 
            - (existingProduct.getMovements() > maxMovements ? commissionAmount : 0.0));

        movementToCreate.setProductId(finalId);
        movementToCreate.setType(movementType);
        movementToCreate.setProductOriginId(originId);
        movementProxy.createMovement(movementToCreate);
        return originCurrentAmount;
    }
    
    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "commissionReportFallback")
    public Mono<CommissionReportResponse> commissionReport(String productId) {
        Flux<Movement> reportResponse = movementProxy.reportCommission(productId, 1);
        Integer commissionMovements = reportResponse.count().block().intValue();
        CommissionReportResponse commissionResponse = new CommissionReportResponse();
        commissionResponse.setTimesAddedCommission(commissionMovements);
        commissionResponse.setTotalCommissionAmount(commissionAmount * commissionMovements);
        return Mono.just(commissionResponse);
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "movementReportFallback")
    public Flux<MovementReportResponse> movementReport(String productId) {
        Flux<Movement> reportResponse = movementProxy.reportMovements(productId, 1);
        MovementReportResponse movementResponse = new MovementReportResponse();
        Flux<MovementReportResponse> reportToReturn = reportResponse
            .groupBy(m -> m.getMovementDate().toLocalDate())
            .flatMap(grouped -> grouped
                .map(Movement::getAmountMoved)
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

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "singlePasiveFallback")
    public Mono<ProductPasive> associateDebitCard(@NonNull String productId, String cardNumber, 
        Boolean isPrincipalAccount) {
        return pasiveRepository.findById(productId)
            .flatMap(existingProduct -> {
                existingProduct.setAffiliateCardDatetime(LocalDateTime.now());
                existingProduct.setDebitCardNumber(cardNumber);
                existingProduct.setIsPrincipalAccount(isPrincipalAccount);
                return pasiveRepository.save(existingProduct);
            });
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "movementsFallback")
    public Flux<Movement> reportLastMovementsDebitCard(String cardNumber) {
        List<String> productsRelated = pasiveRepository
            .findByDebitCardNumber(cardNumber).map(ProductPasive::getId).collectList().block();
        Flux<Movement> lastMovements = movementProxy.getMovementReportByCard(String.join(",", productsRelated), 1);
        return lastMovements;
    }

    @Override
    @CircuitBreaker(name = "product", fallbackMethod = "doubleResponses")
    public Mono<Double> getCurrentBalance(String cardNumber) {
        Double currentAmount = pasiveRepository.findByDebitCardNumber(cardNumber)
            .filter(p -> p.getIsPrincipalAccount()).blockFirst().getCurrentAmount();
        return Mono.just(currentAmount);
    }

    private Mono<ProductPasive> singlePasiveFallback(Throwable throwable){
        System.out.print("Dentro del fallback");
        System.out.println("Throwable: " + throwable.getMessage());
        ProductPasive productPasiveToReturn = new ProductPasive();
        return Mono.just(productPasiveToReturn);
    }
    private Flux<ProductPasive> multiplePasiveFallback(Throwable throwable){
        ProductPasive productPasiveToReturn = new ProductPasive();
        return Flux.just(productPasiveToReturn);
    }
    private Mono<CommissionReportResponse> commissionReportFallback(Throwable throwable){
        CommissionReportResponse commissionReportToReturn = new CommissionReportResponse();
        return Mono.just(commissionReportToReturn);
    }
    private Flux<MovementReportResponse> movementReportFallback(Throwable throwable){
        MovementReportResponse movementReportToReturn = new MovementReportResponse();
        return Flux.just(movementReportToReturn);
    }
    private Flux<Movement> movementsFallback(Throwable throwable){
        Movement movementToReturn = new Movement();
        return Flux.just(movementToReturn);
    }
    private Mono<Double> doubleResponses(Throwable throwable){
        return Mono.just(0.0);
    }
}
