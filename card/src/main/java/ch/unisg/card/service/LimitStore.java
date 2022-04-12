package ch.unisg.card.service;

import java.util.HashMap;

//Implement as Singleton to make sure the values are unique
public class LimitStore {

    private static LimitStore INSTANCE;
    private final HashMap<String,Integer> cardLimits = new HashMap<>();


    public static LimitStore getInstance(){
        if(INSTANCE==null){
            INSTANCE = new LimitStore();
        }
        return INSTANCE;
    }
    //Methods
    public HashMap<String,Integer> getLimits(){
        return this.cardLimits;
    }


    public void updateLimit(String cardNumber, int newLimit){
        if(this.cardLimits.containsKey(cardNumber)){
            this.cardLimits.put(cardNumber,newLimit);
            System.out.println("Updated limit for card "+ cardNumber+ " to be "+String.valueOf(newLimit));
        }
        else{
            this.cardLimits.put(cardNumber,newLimit);
            System.out.println("Added a new card to the system:  "+ cardNumber+ " with Limit "+String.valueOf(newLimit));
        }
    }


}
