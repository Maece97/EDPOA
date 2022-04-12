package ch.unisg.transaction.consumer;

import ch.unisg.transaction.dto.BlockingCheckDto;
import ch.unisg.transaction.dto.PinCheckDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.rest.dto.message.MessageCorrelationResultDto;
import org.camunda.bpm.engine.runtime.MessageCorrelationBuilder;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final RuntimeService runtimeService;

    public MessageCorrelationResult correlateMessagePin(PinCheckDto pinCheckDto, String messageName) {
        try {
            log.info("Consuming pin message {}", messageName);

            MessageCorrelationBuilder messageCorrelationBuilder = runtimeService.createMessageCorrelation(messageName);

            //Need that to prevent concurrent modifications
            Thread.sleep(1000);

            messageCorrelationBuilder.setVariable("pinCorrect",(boolean)pinCheckDto.isPinCorrect());
            MessageCorrelationResult messageResult = messageCorrelationBuilder.processInstanceBusinessKey(pinCheckDto.getCorrelationId())
                    .correlateWithResult();

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
            log.info("Consuming blocking message {}", messageName);

            MessageCorrelationBuilder messageCorrelationBuilder = runtimeService.createMessageCorrelation(messageName);

            Thread.sleep(2000);
            messageCorrelationBuilder.setVariable("checksPassed",(boolean)blockingCheckDto.isChecksPassed());

            MessageCorrelationResult messageResult = messageCorrelationBuilder.processInstanceBusinessKey(blockingCheckDto.getCorrelationId())
                    .correlateWithResult();

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
