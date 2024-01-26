package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase para los tipos de productos pasivos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPasiveType {
    Integer id;

    String description;
}
