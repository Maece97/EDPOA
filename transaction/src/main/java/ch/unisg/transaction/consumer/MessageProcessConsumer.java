package ch.unisg.transaction.consumer;

import ch.unisg.transaction.dto.LimitUpdateDto;
import ch.unisg.transaction.dto.MessageProcessDto;
import ch.unisg.transaction.dto.PinCheckDto;
import ch.unisg.transaction.service.CheckLimitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ch.unisg.transaction.dto.CamundaMessageDto;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProcessConsumer {

    private final RuntimeService runtimeService;
    private final MessageService messageService;
    private final static String MESSAGE_START = "MessageKafkaDemo";
    private final static String MESSAGE_INTERMEDIATE = "MessageIntermediate";

    @KafkaListener(topics = "start-process-message-topic")
    public void startMessageProcess(CamundaMessageDto camundaMessageDto){
        messageService.correlateMessage(camundaMessageDto, MESSAGE_START);
    }

    @KafkaListener(topics = "intermediate-message-topic")
    public void listen(CamundaMessageDto camundaMessageDto){
       messageService.correlateMessage(camundaMessageDto, MESSAGE_INTERMEDIATE);
    }



    @KafkaListener(topics = "check-pin")
    public void checkPin(PinCheckDto pinCheckDto){
        System.out.println("I got something at the receiver side");
        System.out.println(pinCheckDto.getPin());
        //Cool till here everything works...but how the fuck configure this correlation shit??!
        messageService.correlateMessagePin(pinCheckDto, "PinCheckedResult");
    }

    @KafkaListener(topics = "update-limit")
    public void updateLimit(LimitUpdateDto limitUpdateDto){
        //Working fine
        System.out.println("Updating limit via Kafka");
        CheckLimitService checkLimitService = CheckLimitService.getInstance();
        checkLimitService.updateLimit(limitUpdateDto.getCardNumber(), limitUpdateDto.getLimit());
    }
}
