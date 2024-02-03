package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.repository.ProductActiveRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Clase que implementa los métodos de validación adicionales.
 */
@RequiredArgsConstructor
@Service
public class AdditionalValidationServiceImpl implements AdditionalValidationService{
    
    @Autowired
    private final ProductActiveRepository activeRepository;

    @Override
    public Boolean clientHasDebts(String clientId) {
        Flux<ProductActive> clientProductsActive = activeRepository.findByClientId(clientId);
        LocalDate currentDate = LocalDate.now();
        Integer hasProductsWithDebts = clientProductsActive.filter(
            product -> {
                return (product.getPaymentDate().getMonthValue() >= currentDate.getMonthValue()
                    || product.getPaymentDate().getMonthValue() <= currentDate.getMonthValue())
                    && product.getPaymentDate().getDayOfMonth() <= currentDate.getDayOfMonth();
            }
        ).filter(product -> product.getPaymentAmount() > 0).count().block().intValue();

        return hasProductsWithDebts > 0;
    }
    
}
