package ch.unisg.model.domain;

import ch.unisg.model.dto.TransactionTransferObject;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

public class Transaction {
    @Getter
    @Setter
    private UUID id;
    @Getter
    @Setter
    private Timestamp timestamp;
    @Getter
    @Setter
    private String merchant;
    @Getter
    @Setter
    private double amount;
    @Getter
    @Setter
    private String currency;
    @Getter
    @Setter
    private String cardNumber;
    @Getter
    @Setter
    private int pin;

    public Transaction(UUID id, Timestamp timestamp, String merchant, double amount, String currency, String cardNumber, int pin) {
        this.id = id;
        this.timestamp = timestamp;
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public Transaction(TransactionTransferObject tto){
        this.id  =tto.getId();
        this.timestamp = tto.getTimestamp();
        this.merchant = tto.getMerchant();
        this.amount = tto.getAmount();
        this.currency = tto.getCurrency();
        this.cardNumber = tto.getCardNumber();
        this.pin = tto.getPin();
    }

    public void acceptOrDecline(){
        String [] options = {"accept", "decline"};
        int choice = new Random().nextInt(options.length);
        String currentTransaction  = this.getId().toString();
        System.out.println(currentTransaction + ":" + options[choice]);
    }
}
