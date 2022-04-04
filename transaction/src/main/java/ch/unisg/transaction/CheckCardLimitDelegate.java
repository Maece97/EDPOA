package ch.unisg.transaction;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("CheckCardLimit")
public class CheckCardLimitDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
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
