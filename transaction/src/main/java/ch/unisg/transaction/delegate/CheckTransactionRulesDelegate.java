package ch.unisg.transaction.delegate;

import ch.unisg.transaction.dto.PinCheckDto;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("CheckTransactionRules")
@RequiredArgsConstructor
public class CheckTransactionRulesDelegate implements JavaDelegate {

    private final KafkaTemplate<String, PinCheckDto> kafkaTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String countryOfOrigin = (String)delegateExecution.getVariable("country");
        if(countryOfOrigin.equals("GER")){
            System.out.println("checkFailed");
            delegateExecution.setVariable("checksPassed",(boolean) false);
        }else{
            System.out.println("passedAllChecks");
            delegateExecution.setVariable("checksPassed",(boolean) true);
        }

        //Thread.sleep(5000);
        //kafkaTemplate.send("check-pin",new PinCheckDto("20","1234","1234"));

    }
}
