package ch.unisg.transaction.service;

import java.util.HashMap;

//Implement as Singleton to make sure the values are unique
public class CheckLimitService {

    private static CheckLimitService INSTANCE;
    private HashMap<String,Integer> cardLimits = new HashMap<>();

    private CheckLimitService(){
        this.cardLimits = new HashMap<>();
        cardLimits.put("123456",2700);
    }
    public static CheckLimitService getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CheckLimitService();
        }
        return INSTANCE;
    }
    //Methods
    public HashMap<String,Integer> getLimits(){
        return this.cardLimits;
    }

    public boolean limitExceeded(String cardNumber, int amount){
        if(this.cardLimits.containsKey(cardNumber)){
            return this.cardLimits.get(cardNumber)<=amount;
        }
        else return true;
    }

    public void updateLimit(String cardNumber, int newLimit){
        if(this.cardLimits.containsKey(cardNumber)){
            this.cardLimits.put(cardNumber,newLimit);
            System.out.println("Updated limit for card "+ cardNumber+ " to be "+String.valueOf(newLimit));
        }
    }


}
