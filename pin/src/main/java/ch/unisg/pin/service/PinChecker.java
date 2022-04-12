package ch.unisg.pin.service;


import lombok.RequiredArgsConstructor;


public class PinChecker {
    //basic logic to check if the pin is correct
    private String cardNumber;
    private String pin;

    public boolean checkPin(String cardNumber, String pin) throws InterruptedException {
        //Check timeout triggering
        //Thread.sleep(8000);
        return cardNumber.substring(0,4).equals(pin);
    }

}
