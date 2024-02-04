package com.nttdata.bootcamp.productmanagement.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase para armar el request para el microservicio de movimientos.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movement {
    private String id;
    private String productId;
    private Double amountMoved;
    private MovementType type;
    private LocalDateTime movementDate;
    private String clientId;
    private Boolean hasCommission;
    private String productOriginId;
    private Boolean isFromDebitCard;
}
