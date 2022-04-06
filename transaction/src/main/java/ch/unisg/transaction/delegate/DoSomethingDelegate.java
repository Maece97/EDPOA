package ch.unisg.transaction.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;


@Service("DoSomething")
public class DoSomethingDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("I am in the delegate");
        System.out.println(delegateExecution.getVariable("amount"));
        System.out.println("Hit the timeout");

    }
}