package ch.unisg.model.domain;

import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

import ch.unisg.model.KafkaProducer;
import ch.unisg.model.dto.TransactionTransferObject;
import lombok.Data;

@Data
public class Transaction {
    //header
    private UUID id;
    private Timestamp timestamp;
    private int pin;
    //payload
    private String merchant;
    private String merchantCategory;
    private double amount;
    private String currency;
    private String cardNumber;
    private String country;

    private KafkaProducer kafkaProducer;

    public Transaction(UUID id, Timestamp timestamp, String merchant, double amount, String currency, String cardNumber, 
            int pin, String merchantCategory, String country, KafkaProducer kafkaProducer) {
        this.id = id;
        this.timestamp = timestamp;
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.merchantCategory = merchantCategory;
        this.country = country;
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
        this.merchantCategory = tto.getMerchantCategory();
        this.country = tto.getCountry();
        this.kafkaProducer = kafkaProducer;
    }

    public void acceptOrDecline(){

        // mock for demo
        if (this.getMerchantCategory().equalsIgnoreCase("Retail")) {
            System.out.println(this.getId().toString() + ":" + "accept");
            return;
        }
        // mock for demo
        if (this.getMerchant().equalsIgnoreCase("NFT-scam.ru")) {
            System.out.println(this.getId().toString() + ":" + "decline");
            this.kafkaProducer.possibleFraudDetected(this);
            return;
        }

        String [] options = {"accept", "decline"};
        int choice = new Random().nextInt(options.length);
        String currentTransaction  = this.getId().toString();
        System.out.println(currentTransaction + ":" + options[choice]);
        if(choice == 1) {
            this.kafkaProducer.possibleFraudDetected(this);
        }
    }
    
}
