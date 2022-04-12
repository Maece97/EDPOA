package ch.unisg.blockingrules.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlockingChecker {
    public boolean checkBlockingRules(String country,String merchantCategory){
        boolean checksPassed;
        if (country.equals("NG")&&merchantCategory.equals("Bitcoin")){
            checksPassed = false;
        }else checksPassed = true;
        return checksPassed;

    }
}
