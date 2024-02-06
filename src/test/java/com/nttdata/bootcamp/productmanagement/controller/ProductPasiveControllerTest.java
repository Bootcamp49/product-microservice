
package com.nttdata.bootcamp.productmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bootcamp.productmanagement.model.ProductPasive;
import com.nttdata.bootcamp.productmanagement.model.ProductPasiveType;
import com.nttdata.bootcamp.productmanagement.service.ProductPasiveService;
import com.nttdata.bootcamp.productmanagement.util.ConstantsUtil;
import com.nttdata.bootcamp.productmanagement.util.Util;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
class ProductPasiveControllerTest {

    @Autowired
    private ProductPasiveController productPasiveController;

    @MockBean
    private ProductPasiveService productPasiveService;

    private Util util = new Util();

    @Test
    void testCreateProduct() {
        when(productPasiveService.createProduct(any(ProductPasive.class)))
            .thenReturn(Mono.just(productPasiveMockResponse()));
        
        Mono<ProductPasive> responseController = productPasiveController
            .createProduct(productPasiveMockRequest());
        Mono<ProductPasive> responseToCompare = Mono.just(
            util.serializeArchive(ConstantsUtil.createPasiveProductMock_Success, 
            ProductPasive.class)
        );
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testDebitMovement() {
        when(productPasiveService.debitMovement(anyString(), anyDouble(), false))
            .thenReturn(Mono.just(10.0));
        Map<String, Object> requestBody = new HashMap<String, Object>();
        requestBody.put("debitAmount", 5.0);
        requestBody.put("isFromDebitCard", false);
        Mono<Double> responseController = productPasiveController
            .debitMovement("product5678", requestBody);
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseController);
    }

    @Test
    void testDeleteProduct() {
        when(productPasiveService.deleteProduct(anyString()))
            .thenReturn(Mono.empty());
        productPasiveController.deleteProduct("product5678");
        verify(productPasiveService).deleteProduct(anyString());
    }

    @Test
    void testDepositMovement() {
        when(productPasiveService.depositMovement(anyString(), anyDouble()))
            .thenReturn(Mono.just(15.0));
        Map<String, Double> depositAmount = new HashMap<String, Double>();
        depositAmount.put("depositAmount", 5.0);
        Mono<Double> responseController = productPasiveController
            .depositMovement("productId5678", depositAmount);
        assertThat(responseController).usingRecursiveComparison().isEqualTo(Mono.just(15.0));
    }

    @Test
    void testFindById() {
        when(productPasiveService.findById(anyString()))
            .thenReturn(Mono.just(productPasiveMockResponse()));
        Mono<ProductPasive> responseController = productPasiveController.findById("product5678");
        Mono<ProductPasive> responseToCompare = Mono.just(
            util.serializeArchive(
                ConstantsUtil.findPasiveProductByIdMock_Success, ProductPasive.class)
        );
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testFindProducts() {
        when(productPasiveService.findProducts())
            .thenReturn(Flux.just(productPasiveMockResponse()));
        Flux<ProductPasive> responseController = productPasiveController.findProducts();
        Flux<ProductPasive> responseToCompare = Flux.just(
            util.serializeArchive(
                ConstantsUtil.findAllPasiveProductsMock_Success, 
                ProductPasive[].class
        ));
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testFindProductsByClientId() {
        when(productPasiveService.findByClientId(anyString()))
            .thenReturn(Flux.just(productPasiveMockResponse()));
        Flux<ProductPasive> responseController = productPasiveController
            .findProductsByClientId("clientId1234");
        Flux<ProductPasive> responseToCompare = Flux.just(
            util.serializeArchive(ConstantsUtil.findPasiveProductsByClientIdMock_Success, 
            ProductPasive[].class)
        );
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testUpdateProduct() {
        when(productPasiveService.updateProduct(anyString(), any(ProductPasive.class)))
            .thenReturn(Mono.just(productPasiveMockResponse()));
        Mono<ProductPasive> responseController = productPasiveController
            .updateProduct("productId5678", productPasiveMockRequest());
        Mono<ProductPasive> responseToCompare = Mono.just(
            util.serializeArchive(ConstantsUtil.updatePasiveProductMock_Success, 
                ProductPasive.class)
        );
        assertThat(responseController)
            .usingRecursiveComparison().isEqualTo(responseToCompare);

    }

    private ProductPasive productPasiveMockResponse() {
        ProductPasive productPasiveMocked = new ProductPasive();
        ProductPasiveType productPasiveType = new ProductPasiveType();
        productPasiveType.setId(1);
        productPasiveType.setDescription("Ahorro");

        productPasiveMocked.setId("product5678");
        productPasiveMocked.setType(productPasiveType);
        productPasiveMocked.setAccountNumber("45678-45645645-62626");
        productPasiveMocked.setCurrentAmount(10.0);
        productPasiveMocked.setMovements(0);
        productPasiveMocked.setCreationDate(LocalDate.parse("2024-01-28"));
        productPasiveMocked.setClientId("clientId1234");

        return productPasiveMocked;   
    }

    private ProductPasive productPasiveMockRequest() {
        ProductPasive productPasiveMocked = new ProductPasive();
        ProductPasiveType productPasiveType = new ProductPasiveType();
        productPasiveType.setId(1);
        productPasiveType.setDescription("Ahorro");

        productPasiveMocked.setType(productPasiveType);
        productPasiveMocked.setAccountNumber("45678-45645645-62626");
        productPasiveMocked.setCurrentAmount(10.0);
        productPasiveMocked.setClientId("clientId1234");
        return productPasiveMocked;
    }
}
