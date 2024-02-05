package com.nttdata.bootcamp.productmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nttdata.bootcamp.productmanagement.model.ProductActive;
import com.nttdata.bootcamp.productmanagement.model.ProductActiveType;
import com.nttdata.bootcamp.productmanagement.service.ProductActiveService;
import com.nttdata.bootcamp.productmanagement.util.ConstantsUtil;
import com.nttdata.bootcamp.productmanagement.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
class ProductActiveControllerTest {

    @Autowired
    private ProductActiveController productActiveController;

    @MockBean
    private ProductActiveService productActiveService;

    private Util util = new Util();

    @Test
    void testConsumeCredit() {
        when(productActiveService.consumeCredit(anyString(), anyDouble()))
            .thenReturn(Mono.just(10.0));
        Mono<Double> responseController = productActiveController
            .consumeCredit("productId1234", 5.0);
        assertThat(responseController).usingRecursiveComparison().isEqualTo(Mono.just(10.0));
    }

    @Test
    void testCreateProduct() {
        when(productActiveService.createProduct(any(ProductActive.class)))
            .thenReturn(Mono.just(productActiveMockResponse()));
        
        Mono<ProductActive> responseController = productActiveController
            .createProduct(productActiveMockRequest());
        Mono<ProductActive> responseToCompare = Mono.just(
            util.serializeArchive(ConstantsUtil.createActiveProductMock_Success, 
            ProductActive.class)
        );
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testDeleteProduct() {
        when(productActiveService.deleteProduct(anyString()))
            .thenReturn(Mono.empty());
        productActiveController.deleteProduct("12345");
        verify(productActiveService).deleteProduct(anyString());
    }

    @Test
    void testFindById() {
        when(productActiveService.findById(anyString()))
            .thenReturn(Mono.just(productActiveMockResponse()));
        Mono<ProductActive> responseController = productActiveController.findById("productId1234");
        Mono<ProductActive> responseToCompare = Mono.just(
            util.serializeArchive(
                ConstantsUtil.findActiveProductByIdMock_Success, ProductActive.class
            )
        );
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testFindProducts() {
        when(productActiveService.findProducts())
            .thenReturn(Flux.just(productActiveMockResponse()));
        Flux<ProductActive> responseController = productActiveController.findProducts();
        Flux<ProductActive> responseToCompare = Flux.just(
            util.serializeArchive(ConstantsUtil.findAllActiveProductsMock_Success,
            ProductActive[].class)
        );
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testFindProductsByClientId() {
        when(productActiveService.findByClientId(anyString()))
            .thenReturn(Flux.just(productActiveMockResponse()));
        Flux<ProductActive> responseController = productActiveController
            .findProductsByClientId("clientId1234");
        Flux<ProductActive> responseToCompare = Flux.just(
            util.serializeArchive(ConstantsUtil.findActiveProductsByClientIdMock_Success, 
            ProductActive[].class));
        assertThat(responseController).usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    @Test
    void testPayCredit() {
        when(productActiveService.payCredit(anyString(), anyDouble(), anyString()))
            .thenReturn(Mono.just(15.0));
        Mono<Double> responseController = productActiveController
            .payCredit("productId1234", 5.0, "cardNumber");
        assertThat(responseController).usingRecursiveComparison().isEqualTo(Mono.just(15.0));
    }

    @Test
    void testUpdateProduct() {
        when(productActiveService.updateProduct(anyString(), any(ProductActive.class)))
            .thenReturn(Mono.just(productActiveMockResponse()));
        Mono<ProductActive> responseController = productActiveController
            .updateProduct("productId1234", productActiveMockRequest());
        Mono<ProductActive> responseToCompare = Mono.just(
            util.serializeArchive(
                ConstantsUtil.updateActiveProductMock_Success, 
                ProductActive.class)
        );
        assertThat(responseController)
            .usingRecursiveComparison().isEqualTo(responseToCompare);
    }

    private ProductActive productActiveMockResponse() {
        ProductActive productActiveMocked = new ProductActive();
        ProductActiveType productActiveType = new ProductActiveType();
        productActiveType.setId(1);
        productActiveType.setDescription("Personal");

        productActiveMocked.setId("productId1234");
        productActiveMocked.setAccountNumber("1234-12312312-51515");
        productActiveMocked.setClientId("clientId1234");
        productActiveMocked.setCreditCardNumber("1234-1234561-31314");
        productActiveMocked.setCreditLine(1600.0);
        productActiveMocked.setCurrentCredit(1500.0);
        productActiveMocked.setType(productActiveType);
        return productActiveMocked;
    }

    private ProductActive productActiveMockRequest() {
        ProductActive productActiveMocked = new ProductActive();
        ProductActiveType productActiveType = new ProductActiveType();
        productActiveType.setId(1);
        productActiveType.setDescription("Personal");

        productActiveMocked.setAccountNumber("1234-12312312-51515");
        productActiveMocked.setClientId("clientId1234");
        productActiveMocked.setCreditCardNumber("1234-1234561-31314");
        productActiveMocked.setCreditLine(1600.0);
        productActiveMocked.setCurrentCredit(1500.0);
        productActiveMocked.setType(productActiveType);
        return productActiveMocked;
    }
}
