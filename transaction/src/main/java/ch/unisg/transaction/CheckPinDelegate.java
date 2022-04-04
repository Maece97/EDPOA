package ch.unisg.transaction;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service("CheckPin")
public class CheckPinDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Thread.sleep(10000);
        System.out.println("I am checking the pin here");
        System.out.println(delegateExecution.getVariable("pin"));

        String pin = (String)delegateExecution.getVariable("pin");
        if(pin.equals("123")){
            delegateExecution.setVariable("pinCorrect", (boolean)true);
            System.out.println("pin was correct");
        }else{
            delegateExecution.setVariable("pinCorrect",(boolean)false);
            System.out.println("pin was incorrect");
        }

    }
}
