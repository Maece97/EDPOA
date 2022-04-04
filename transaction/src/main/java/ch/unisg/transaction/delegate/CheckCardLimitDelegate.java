package ch.unisg.transaction.delegate;


import ch.unisg.transaction.dto.CamundaMessageDto;
import ch.unisg.transaction.dto.LimitUpdateDto;
import ch.unisg.transaction.dto.MessageProcessDto;
import ch.unisg.transaction.service.CheckLimitService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;

@Service("CheckCardLimit")
@RequiredArgsConstructor
public class CheckCardLimitDelegate implements JavaDelegate {
    private final KafkaTemplate<String, LimitUpdateDto> kafkaTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        // test update via Kafka
        System.out.println("Sending something here via Kafka");
        kafkaTemplate.send("update-limit",new LimitUpdateDto("123456",9899));
        //Get cardNumber and amount
        int amount = Integer.valueOf((String)delegateExecution.getVariable("amount"));
        String cardNumber = (String) delegateExecution.getVariable("cardNumber");
        //check if limit was exceeded
        CheckLimitService limitChecker = CheckLimitService.getInstance();
        boolean exceeded = limitChecker.limitExceeded(cardNumber,amount);
        System.out.println("Was the limit exceeded?");
        System.out.println(exceeded);
        //set variable to process
        delegateExecution.setVariable("limitExceeded", (boolean)exceeded);
    }
}
