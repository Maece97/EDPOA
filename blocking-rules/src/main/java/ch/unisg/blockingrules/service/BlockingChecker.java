package ch.unisg.blockingrules.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockingChecker {
    public boolean checkBlockingRules(String country){
        boolean checksPassed;
        if (country.equals("GER")){
            checksPassed = false;
        }else checksPassed = true;
        return checksPassed;

    }
}
