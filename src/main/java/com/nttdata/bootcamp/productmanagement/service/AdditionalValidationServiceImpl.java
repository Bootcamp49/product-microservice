package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.repository.ProductActiveRepository;
import com.nttdata.bootcamp.productmanagement.repository.ProductPasiveRepository;
import java.time.LocalDate;
import java.util.Comparator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Clase que implementa los métodos de validación adicionales.
 */
@RequiredArgsConstructor
@Service
public class AdditionalValidationServiceImpl implements AdditionalValidationService {
    
    @Autowired
    private final ProductActiveRepository activeRepository;
    private final ProductPasiveRepository pasiveRepository;

    @Override
    public Boolean clientHasDebts(String clientId) {
        Flux<ProductActive> clientProductsActive = activeRepository.findByClientId(clientId);
        Integer hasProductsWithDebts = 0;
        if (clientProductsActive.count().block() > 0) {
            LocalDate currentDate = LocalDate.now();
            hasProductsWithDebts = clientProductsActive.filter(
                product -> {
                    return (product.getPaymentDate().getMonthValue() >= currentDate.getMonthValue()
                        || product.getPaymentDate().getMonthValue() <= currentDate.getMonthValue())
                        && product.getPaymentDate().getDayOfMonth() <= currentDate.getDayOfMonth();
                }
            ).filter(product -> product.getPaymentAmount() > 0).count().block().intValue();
        }
        return hasProductsWithDebts > 0;
    }

    @Override
    public ProductPasive productToMakeDebitPay(@NonNull String productId, Double amountToConsume) {
        String debitCardNumber = pasiveRepository.findById(productId)
            .map(existingProduct -> existingProduct.getDebitCardNumber()).block();

        ProductPasive productToReturn = pasiveRepository.findByDebitCardNumber(debitCardNumber)
            .filter(p -> p.getCurrentAmount() >= amountToConsume)
            .sort(Comparator.comparing(ProductPasive::getAffiliateCardDatetime))
            .blockFirst();
        
        return productToReturn;
    }

    @Override
    public Boolean productPasiveValidToPay(@NonNull String productId, Double amountToConsume) {
        Boolean response = false;
        response = pasiveRepository.findById(productId)
            .filter(existingProduct -> existingProduct.getCurrentAmount() >= amountToConsume
                && (existingProduct.getDebitCardNumber() != null 
                && !existingProduct.getDebitCardNumber().trim().isEmpty()))
            .block() != null;
        return response;
    }
    
}
