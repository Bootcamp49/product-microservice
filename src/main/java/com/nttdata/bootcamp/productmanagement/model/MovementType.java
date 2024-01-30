package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase para los tipos de movimientos.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovementType {
    Integer id;

    String description;
}
