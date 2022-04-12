package ch.unisg.transaction.consumer;

import ch.unisg.transaction.dto.*;
import ch.unisg.transaction.service.CheckLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProcessConsumer {

    private final RuntimeService runtimeService;
    private final MessageService messageService;
    private final static String MESSAGE_START = "MessageKafkaDemo";
    private final static String MESSAGE_INTERMEDIATE = "MessageIntermediate";



    //Get the results from checking the pin
    @KafkaListener(topics = "check-pin-result")
    public void checkPin(@Payload Object pinCheckDto){
        System.out.println("I got something at the receiver side");
        System.out.println(pinCheckDto.getClass());
        ConsumerRecord pinCheckDto1 = (ConsumerRecord) pinCheckDto;
        LinkedHashMap lhm = (LinkedHashMap) pinCheckDto1.value();
        System.out.println("casted");
        PinCheckDto pto = new PinCheckDto(lhm);
        System.out.println(pto);
        messageService.correlateMessagePin(pto, "PinCheckedResult");
    }

    @KafkaListener(topics = "update-limit")
    public void updateLimit(LimitUpdateDto limitUpdateDto){
        //Working fine
        System.out.println("Updating limit via Kafka");
        CheckLimitService checkLimitService = CheckLimitService.getInstance();
        checkLimitService.updateLimit(limitUpdateDto.getCardNumber(), limitUpdateDto.getLimit());
    }

    @KafkaListener(topics = "check-blocking-result")
    public void checkBlocking(@Payload Object blocking){
        System.out.println("Blocking");
        System.out.println(blocking);
    }


}
