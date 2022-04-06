package ch.unisg.pin.service;

public class CheckPinService {


    public boolean checkPin(String pin, String cardNumber){
        return pin.equals(cardNumber.substring(0,4));

    }
}
