package ch.unisg.transactiondispute.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;

import ch.unisg.transactiondispute.KafkaProducer;
import ch.unisg.transactiondispute.dto.TransactionTransferObject;

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

    // Mock function to simulate handling of a dispute
    public void handleDispute(){
        String currentTransaction = this.getId().toString();
        System.out.println("Dispute resolved! Sending information to the model");
        this.kafkaProducer.newFraudulentTransaction(this);
    }
}
