
package com.nttdata.bootcamp.productmanagement.controller;

import java.time.LocalDate;
import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.model.ProductPasiveType;
import com.nttdata.bootcamp.productmanagement.service.ProductPasiveService;
import com.nttdata.bootcamp.productmanagement.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProductPasiveControllerTest {

    @Autowired
    private ProductPasiveController productPasiveController;

    @MockBean
    private ProductPasiveService productPasiveService;

    private Util util = new Util();

    @Test
    void testCreateProduct() {

    }

    @Test
    void testDebitMovement() {

    }

    @Test
    void testDeleteProduct() {

    }

    @Test
    void testDepositMovement() {

    }

    @Test
    void testFindById() {

    }

    @Test
    void testFindProducts() {

    }

    @Test
    void testFindProductsByClientId() {

    }

    @Test
    void testUpdateProduct() {

    }

    private ProductPasive productPasiveMockResponse() {
        ProductPasive productPasiveMocked = new ProductPasive();
        ProductPasiveType productPasiveType = new ProductPasiveType();
        productPasiveType.setId(1);
        productPasiveType.setDescription("Ahorro");

        productPasiveMocked.setId("probe");
        productPasiveMocked.setType(productPasiveType);
        productPasiveMocked.setAccountNumber("accountNumber123");
        productPasiveMocked.setCurrentAmount(10.0);
        productPasiveMocked.setMovements(0);
        productPasiveMocked.setCreationDate(LocalDate.parse("2024-01-28"));
        productPasiveMocked.setClientId("clientId1234");

        return productPasiveMocked;   
    }

    private ProductPasive productPasiveMockRequest() {
        ProductPasive productPasiveMocked = new ProductPasive();
        return productPasiveMocked;
    }
}
