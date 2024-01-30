package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase para recibir el request de las transferencias.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    private Double transferAmount;

    private String finalProductId;
    
}
