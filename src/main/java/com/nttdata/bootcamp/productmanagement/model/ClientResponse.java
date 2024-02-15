package com.nttdata.bootcamp.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {
    private String id;
    private String name;
    private String lastName;
    private String documentNumber;
    private String password;
    private String clientType;
    private String cellPhoneNumber;
    private String imei;
    private String email;
}
