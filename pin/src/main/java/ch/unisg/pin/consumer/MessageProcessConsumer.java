package ch.unisg.pin.consumer;

import ch.unisg.pin.dto.PinCheckDto;
import ch.unisg.pin.service.PinChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProcessConsumer {

   private final KafkaTemplate<String,PinCheckDto> kafkaTemplate;

    @KafkaListener(topics = "check-pin")
    public void checkPin(@Payload PinCheckDto pinCheckDto) throws InterruptedException {
        System.out.println("Checking the pin you sent me");
        //Check pin
        PinChecker pinChecker = new PinChecker();
        boolean pinCorrect = pinChecker.checkPin(pinCheckDto.getCardNumber(),pinCheckDto.getPin());
        //set the Pin to the DTO
        pinCheckDto.setPinCorrect(pinCorrect);
        //send back via Kafka
        kafkaTemplate.send("check-pin-result",pinCheckDto);

    }


}
