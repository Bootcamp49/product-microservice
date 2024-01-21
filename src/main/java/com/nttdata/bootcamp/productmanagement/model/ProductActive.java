package com.nttdata.bootcamp.productmanagement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Double creditLine;

    private String creditCardNumber;

    private String cliendId;
}
