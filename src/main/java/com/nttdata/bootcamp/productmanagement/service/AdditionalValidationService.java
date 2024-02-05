package com.nttdata.bootcamp.productmanagement.service;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;

/**
 * Interface para validaciones adicionales.
 */
public interface AdditionalValidationService {
    Boolean clientHasDebts(String clientId);

    ProductPasive productToMakeDebitPay(String productId, Double amountToConsume);

    Boolean productPasiveValidToPay(String productId, Double amountToConsume);
}
