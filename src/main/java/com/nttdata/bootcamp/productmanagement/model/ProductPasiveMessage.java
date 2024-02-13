package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPasiveMessage {
    private String originId;
    private Double transferAmount;
    private String finalProductId;
    private Boolean toPasiveProduct;
}
