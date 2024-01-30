package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Clase para la colección de Productos activos.
 */
@Document(collection = "productActive")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductActive {

    @Id
    private String id;

    private ProductActiveType type;

    private String accountNumber;

    private Double currentCredit;

    private Integer movements;

    private Double creditLine;

    private String creditCardNumber;

    private String clientId;

}
