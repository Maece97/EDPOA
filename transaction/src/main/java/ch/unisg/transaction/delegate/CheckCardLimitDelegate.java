package ch.unisg.transaction.delegate;


import ch.unisg.transaction.dto.CamundaMessageDto;
import ch.unisg.transaction.dto.MessageProcessDto;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service("CheckCardLimit")
@RequiredArgsConstructor
public class CheckCardLimitDelegate implements JavaDelegate {
    private final KafkaTemplate<String, CamundaMessageDto> kafkaTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        //kafkaTemplate.send("example-topic",new CamundaMessageDto("MessageIntermediate",new MessageProcessDto("Hi",10.1,true,false)));
        System.out.println("sent kafka message");
        int limit = 1000;
        int requested = Integer.valueOf((String)delegateExecution.getVariable("amount"));
        System.out.println("Req"+ (String) delegateExecution.getVariable("amount"));

        if(requested>limit){
            delegateExecution.setVariable("limitExceeded", (boolean)true);
            System.out.println("Limit was exceeded");
        }else{
            delegateExecution.setVariable("limitExceeded", (boolean)false);
            System.out.println("Limit was not exceeded");

        }
    }
}
