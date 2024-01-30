package com.nttdata.bootcamp.productmanagement.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase para armar la respuesta del reporte de movimientos.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovementReportResponse {

    private LocalDate day;

    private Double dayAmount;
    
}
