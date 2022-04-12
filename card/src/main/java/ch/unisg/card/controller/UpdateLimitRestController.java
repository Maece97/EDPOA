package ch.unisg.card.controller;

import ch.unisg.card.dto.LimitUpdateDto;
import ch.unisg.card.service.LimitStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
class UpdateLimitRestController {

    private final KafkaTemplate<String,LimitUpdateDto> kafkaTemplate;


    @PostMapping("/update")
    public void startMessageProcess(@RequestBody LimitUpdateDto limitUpdateDto){
        LimitStore limitStore  = LimitStore.getInstance();
        limitStore.updateLimit(limitUpdateDto.getCardNumber(), limitUpdateDto.getLimit());
        System.out.println("Updated limit in limit store. Store currently is like:");
        System.out.println(limitStore.getLimits());
        kafkaTemplate.send("update-limit",limitUpdateDto);
    }

}