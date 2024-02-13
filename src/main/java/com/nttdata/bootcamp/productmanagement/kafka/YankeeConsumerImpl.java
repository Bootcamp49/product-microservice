package com.nttdata.bootcamp.productmanagement.kafka;

import com.google.gson.Gson;
import com.nttdata.bootcamp.productmanagement.model.ProductPasiveEvent;
import com.nttdata.bootcamp.productmanagement.service.ProductActiveService;
import com.nttdata.bootcamp.productmanagement.service.ProductPasiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class YankeeConsumerImpl implements YankeeConsumer{

    @Autowired
    private ProductPasiveService productPasiveService;
    @Autowired
    private ProductActiveService productActiveService;
    @Override
    @KafkaListener(
            topics = {"create_movement", "pay_credit"},
            groupId = "client"
    )
    public void consume(String event) {
        ProductPasiveEvent productPasiveEvent = new Gson().fromJson(event, ProductPasiveEvent.class);
        if(productPasiveEvent.getProductPasive().getToPasiveProduct()){
            productPasiveService.transfer(
                    productPasiveEvent.getProductPasive().getOriginId(),
                    productPasiveEvent.getProductPasive().getTransferAmount(),
                    productPasiveEvent.getProductPasive().getFinalProductId()
            );
        }else {
            productActiveService.payCredit(
                    productPasiveEvent.getProductPasive().getFinalProductId(),
                    productPasiveEvent.getProductPasive().getTransferAmount(),
                    productPasiveEvent.getProductPasive().getOriginId()
            );
        }

    }
}
