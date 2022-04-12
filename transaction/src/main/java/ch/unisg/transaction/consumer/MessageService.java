package ch.unisg.transaction.consumer;

import ch.unisg.transaction.dto.BlockingCheckDto;
import ch.unisg.transaction.dto.PinCheckDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ch.unisg.transaction.dto.CamundaMessageDto;
import ch.unisg.transaction.util.VariablesUtil;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.dto.message.MessageCorrelationResultDto;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final RuntimeService runtimeService;

    public MessageCorrelationResult correlateMessage(CamundaMessageDto camundaMessageDto, String messageName) {
        try {
            log.info("Consuming message {}", messageName);

            MessageCorrelationBuilder messageCorrelationBuilder = runtimeService.createMessageCorrelation(messageName);

            if (camundaMessageDto.getDto() != null) {
                Map<String, Object> variables = VariablesUtil.toVariableMap(camundaMessageDto.getDto());
                messageCorrelationBuilder.setVariables(VariablesUtil.toVariableMap(camundaMessageDto.getDto()));
            }

            MessageCorrelationResult messageResult = messageCorrelationBuilder.processInstanceBusinessKey(camundaMessageDto.getCorrelationId())
                    .correlateWithResult();

            String messageResultJson = new ObjectMapper().writeValueAsString(MessageCorrelationResultDto.fromMessageCorrelationResult(messageResult));

            log.info("Correlation successful. Process Instance Id: {}", messageResultJson);
            log.info("Correlation key used: {}", camundaMessageDto.getCorrelationId());

            return messageResult;
        } catch (MismatchingMessageCorrelationException e) {
            log.error("Issue when correlating the message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unknown issue occurred", e);
        }
        return null;
    }

    public MessageCorrelationResult correlateMessagePin(PinCheckDto pinCheckDto, String messageName) {
        try {
            log.info("Consuming message {}", messageName);
            log.info("Pin");

            MessageCorrelationBuilder messageCorrelationBuilder = runtimeService.createMessageCorrelation(messageName);

            Thread.sleep(1000);
            //messageCorrelationBuilder.setVariable("pin",pinCheckDto.getPin());
            //messageCorrelationBuilder.setVariable("cardNumber",pinCheckDto.getCardNumber());
            messageCorrelationBuilder.setVariable("pinCorrect",(boolean)pinCheckDto.isPinCorrect());

            System.out.println("Correlating here in progress");
            System.out.println(pinCheckDto.getCorrelationId());
            MessageCorrelationResult messageResult = messageCorrelationBuilder.processInstanceBusinessKey(pinCheckDto.getCorrelationId())
                    .correlateWithResult();
            System.out.println("Correlated");

            String messageResultJson = new ObjectMapper().writeValueAsString(MessageCorrelationResultDto.fromMessageCorrelationResult(messageResult));

            log.info("Correlation pin successful. Process Instance Id: {}", messageResultJson);
            log.info("Correlation key used: {}", pinCheckDto.getCorrelationId());

            return messageResult;
        } catch (MismatchingMessageCorrelationException e) {
            log.error("Issue when correlating the message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unknown issue occurred", e);
        }
        return null;
    }

    public MessageCorrelationResult correlateMessageBlocking(BlockingCheckDto blockingCheckDto, String messageName) {
        try {
            log.info("Consuming message {}", messageName);
            log.info("Blocking");

            MessageCorrelationBuilder messageCorrelationBuilder = runtimeService.createMessageCorrelation(messageName);

            Thread.sleep(2000);
            messageCorrelationBuilder.setVariable("checksPassed",(boolean)blockingCheckDto.isChecksPassed());

            System.out.println("Correlating here in progress");
            System.out.println(blockingCheckDto.getCorrelationId());
            MessageCorrelationResult messageResult = messageCorrelationBuilder.processInstanceBusinessKey(blockingCheckDto.getCorrelationId())
                    .correlateWithResult();
            System.out.println("Correlated");

            String messageResultJson = new ObjectMapper().writeValueAsString(MessageCorrelationResultDto.fromMessageCorrelationResult(messageResult));

            log.info("Correlation blocking successful. Process Instance Id: {}", messageResultJson);
            log.info("Correlation key used: {}", blockingCheckDto.getCorrelationId());

            return messageResult;
        } catch (MismatchingMessageCorrelationException e) {
            log.error("Issue when correlating the message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unknown issue occurred", e);
        }
        return null;
    }
}
