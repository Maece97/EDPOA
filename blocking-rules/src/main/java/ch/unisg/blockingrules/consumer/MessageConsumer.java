package ch.unisg.blockingrules.consumer;

import ch.unisg.blockingrules.BlockingRulesApplication;
import ch.unisg.blockingrules.dto.BlockingCheckDto;
import ch.unisg.blockingrules.service.BlockingChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final KafkaTemplate<String,BlockingCheckDto> kafkaTemplate;

    @KafkaListener(topics = "check-blocking")
    public void checkBlocking(@Payload BlockingCheckDto blockingCheckDto){
        System.out.println("Checking the blocking rules");
        System.out.println(blockingCheckDto);
        //Check pin
        BlockingChecker blockingChecker = new BlockingChecker();
        boolean checksPassed = blockingChecker.checkBlockingRules(blockingCheckDto.getCountry());
        blockingCheckDto.setChecksPassed(checksPassed);
        //send back via Kafka
        kafkaTemplate.send("check-blocking-result",blockingCheckDto);

    }


}
