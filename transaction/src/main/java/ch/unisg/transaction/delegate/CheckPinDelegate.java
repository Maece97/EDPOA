package ch.unisg.transaction.delegate;

import camundajar.impl.scala.concurrent.impl.FutureConvertersImpl;
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
        //Get all values from the process
        String pin = (String)delegateExecution.getVariable("pin");
        String cardNumber = (String) delegateExecution.getVariable("cardNumber");
        String correlationId = (String) delegateExecution.getBusinessKey();
        //Just set some default value here --> avoids creating a separate DTO
        boolean pinCorrect = false;

        //Construct a new DTO to publish via Kafka
        PinCheckDto pinCheckDto = new PinCheckDto(correlationId,cardNumber,pin,pinCorrect);

        //Send via Kafka
        kafkaTemplate.send("check-pin", pinCheckDto);


    }
}
