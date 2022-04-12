package ch.unisg.transaction.delegate;

import ch.unisg.transaction.dto.BlockingCheckDto;
import ch.unisg.transaction.dto.PinCheckDto;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("CheckBlockingRules")
@RequiredArgsConstructor
public class CheckBlockingRulesDelegate implements JavaDelegate {


    private final KafkaTemplate<String, BlockingCheckDto> kafkaTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("Trigger blocking rules checking");
        //Read all necessary values from the process
        String businessKey = delegateExecution.getBusinessKey();
        String country = (String)delegateExecution.getVariable("country");
        String merchantCategory = (String)delegateExecution.getVariable("merchantCategory");
        boolean checksPassed = false;
        //Create DTO
        BlockingCheckDto blockingCheckDto = new BlockingCheckDto(businessKey,country,merchantCategory,checksPassed);
        //Publish DTO via Kafka
        kafkaTemplate.send("check-blocking",blockingCheckDto);

    }
}
