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

import javax.ws.rs.core.Link;
import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final MessageService messageService;

    //Get the results from checking the pin
    @KafkaListener(topics = "check-pin-result")
    public void checkPin(@Payload Object rawPinCheckDto){
        //Trouble with receiving DTOs via Kafka->receive generic object and cast here
        System.out.println("Got pin result");
        LinkedHashMap rawData = (LinkedHashMap)((ConsumerRecord)rawPinCheckDto).value();
        PinCheckDto pinCheckDto = new PinCheckDto(rawData);
        messageService.correlateMessagePin(pinCheckDto, "PinCheckedResult");
    }


    @KafkaListener(topics = "check-blocking-result")
    public void checkBlocking(@Payload Object rawBlockingCheckDto){
        System.out.println("Got blocking result");
        //Trouble with receiving DTOs via Kafka->receive generic object and cast here
        LinkedHashMap rawData = (LinkedHashMap)((ConsumerRecord)rawBlockingCheckDto).value();
        BlockingCheckDto blockingCheckDto = new BlockingCheckDto(rawData);
        messageService.correlateMessageBlocking(blockingCheckDto,"BlockingCheckedResult");
    }

    @KafkaListener(topics = "update-limit")
    public void updateLimit(@Payload Object rawLimitUpdate){
        System.out.println("Updating Limit here");
        LinkedHashMap rawData = (LinkedHashMap)((ConsumerRecord)rawLimitUpdate).value();
        //parse data from the DTO
        String cardNumber = String.valueOf(rawData.get("cardNumber"));
        int limit = Integer.parseInt(String.valueOf(rawData.get("limit")));
        //Set the new limit in the singleton
        CheckLimitService checkLimitService = CheckLimitService.getInstance();
        checkLimitService.updateLimit(cardNumber, limit);
    }

    @KafkaListener(topics = "approved-transactions")
    public void test(@Payload Object rawDataa){
        //Just for testing purposes --- Can be commented out
        LinkedHashMap rawData = (LinkedHashMap)((ConsumerRecord)rawDataa).value();
        System.out.println("#######GOT THE TRANSACTION######");
        System.out.println(rawData);

    }


}
