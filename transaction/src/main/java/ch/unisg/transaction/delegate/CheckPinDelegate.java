package ch.unisg.transaction.delegate;

import ch.unisg.transaction.dto.CamundaMessageDto;
import ch.unisg.transaction.dto.MessageProcessDto;
import ch.unisg.transaction.dto.PinCheckDto;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("CheckPin")
@RequiredArgsConstructor
public class CheckPinDelegate implements JavaDelegate {
    private final KafkaTemplate<String, PinCheckDto> kafkaTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("I am checking the pin here");
        String pin = (String)delegateExecution.getVariable("pin");
        String cardNumber = (String) delegateExecution.getVariable("cardNumber");
        kafkaTemplate.send("check-pin",new PinCheckDto("1",cardNumber,pin));

        if(pin.equals("123")){
            delegateExecution.setVariable("pinCorrect", (boolean)true);
            System.out.println("pin was correct");
        }else{
            delegateExecution.setVariable("pinCorrect",(boolean)false);
            System.out.println("pin was incorrect");
        }

    }
}
