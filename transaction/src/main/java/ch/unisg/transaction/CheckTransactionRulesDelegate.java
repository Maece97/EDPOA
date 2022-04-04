package ch.unisg.transaction;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("CheckTransactionRules")
public class CheckTransactionRulesDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String countryOfOrigin = (String)delegateExecution.getVariable("country");
        if(countryOfOrigin.equals("GER")){
            System.out.println("checkFailed");
            delegateExecution.setVariable("checksPassed",(boolean) false);
        }else{
            System.out.println("passedAllChecks");
            delegateExecution.setVariable("checksPassed",(boolean) true);
        }
    }
}
