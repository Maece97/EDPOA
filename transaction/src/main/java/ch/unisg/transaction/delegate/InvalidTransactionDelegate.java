package ch.unisg.transaction.delegate;

import ch.unisg.transaction.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("InvalidTransaction")
@Component
@RequiredArgsConstructor
@Slf4j
public class InvalidTransactionDelegate implements JavaDelegate {
    private final KafkaTemplate<String, TransactionDto> kafkaTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("The transaction was rejected");
        //Get variables from execution context
        double amount = Double.valueOf((String)delegateExecution.getVariable("amount"));
        int pin = Integer.valueOf((String)delegateExecution.getVariable("pin"));
        String cardNumber = (String)delegateExecution.getVariable("cardNumber");
        String country = (String)delegateExecution.getVariable("country");
        String merchant = (String)delegateExecution.getVariable("merchant");
        String merchantCategory = (String)delegateExecution.getVariable("merchantCategory");
        String currency = (String)delegateExecution.getVariable("currency");
        String status = (String)delegateExecution.getVariable("status");
        String exchangeRate = (String) delegateExecution.getVariable("exchangeRate");
        //build DTO
        TransactionDto transactionDto = new TransactionDto(merchant,merchantCategory,country,amount,currency,cardNumber,pin,status,exchangeRate,"rejected");
        //Send DTO to approved transactions via Kafka
        kafkaTemplate.send("approved-transactions", transactionDto);



    }
}
