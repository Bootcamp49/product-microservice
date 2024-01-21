package com.nttdata.bootcamp.productmanagement.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
}
