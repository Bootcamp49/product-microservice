package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase para el tipo de productos activos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductActiveType {
    Integer id;

    String description;
}
