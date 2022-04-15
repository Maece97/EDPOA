package ch.unisg.fraudinvestigation.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

import ch.unisg.fraudinvestigation.KafkaProducer;
import ch.unisg.fraudinvestigation.dto.TransactionTransferObject;

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

    private KafkaProducer kafkaProducer;

    public Transaction(UUID id, Timestamp timestamp, String merchant, double amount, String currency, String cardNumber, int pin, KafkaProducer kafkaProducer) {
        this.id = id;
        this.timestamp = timestamp;
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.kafkaProducer = kafkaProducer;
    }

    public Transaction(TransactionTransferObject tto, KafkaProducer kafkaProducer){
        this.id = tto.getId();
        this.timestamp = tto.getTimestamp();
        this.merchant = tto.getMerchant();
        this.amount = tto.getAmount();
        this.currency = tto.getCurrency();
        this.cardNumber = tto.getCardNumber();
        this.pin = tto.getPin();
        this.kafkaProducer = kafkaProducer;
    }

    // Mock function to simulate if fraud was real or not
    // In reality a customer agent would call the card holder and ask them about the transaction
    public void acceptOrDecline(){
        String [] options = {"fraud", "noFraud"};
        int choice = new Random().nextInt(options.length);
        String currentTransaction  = this.getId().toString();
        System.out.println(currentTransaction + ":" + options[choice]);
        if(choice == 0) {
            System.out.println("Fraud confirmed! Creating fraud dispute.");
            this.kafkaProducer.createFraudDispute(this);
        }
    }

    
}
