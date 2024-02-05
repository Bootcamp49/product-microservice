package com.nttdata.bootcamp.productmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Clase para la colección de productos pasivos de la BD.
 */
@Document(collection = "productPasive")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPasive {

    @Id
    private String id;

    private ProductPasiveType type;

    private String accountNumber;

    private Double currentAmount;

    private Integer movements;

    private LocalDate creationDate;

    private String clientId;

    private String debitCardNumber;

    private LocalDateTime affiliateCardDatetime;
    
    private Boolean isPrincipalAccount;
}
