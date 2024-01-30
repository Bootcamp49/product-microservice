package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase para la respuesta del reporte de comisiones.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionReportResponse {

    private Integer timesAddedCommission;

    private Double totalCommissionAmount;
    
}
