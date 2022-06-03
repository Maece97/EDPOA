package ch.unisg.transaction.delegate;

import ch.unisg.transaction.controller.ResendTransactionController;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("Timeout")
public class TimeoutDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("#### The approval process timed out - we will retry to authenticate now#####");
        System.out.println("#### THANK YOU for your patience!!!###");
        //Get all process vars to resend the current transaction
        String businessKey = delegateExecution.getBusinessKey();
        String amount = (String)delegateExecution.getVariable("amount");
        String pin = (String)delegateExecution.getVariable("pin");
        String cardNumber = (String)delegateExecution.getVariable("cardNumber");
        String country = (String)delegateExecution.getVariable("country");
        String merchant = (String)delegateExecution.getVariable("merchant");
        String merchantCategory = (String)delegateExecution.getVariable("merchantCategory");
        String currency = (String)delegateExecution.getVariable("currency");
        String tries = (String)delegateExecution.getVariable("tries");
        String status = (String)delegateExecution.getVariable("status");
        String exchangeRate = (String) delegateExecution.getVariable("exchangeRate");
        //Resend the transaction here
        ResendTransactionController resendTransactionController = new ResendTransactionController();
        resendTransactionController.resendTransaction(businessKey,amount,pin,cardNumber,country,merchant,merchantCategory,
                currency,tries,status,exchangeRate);

    }
}