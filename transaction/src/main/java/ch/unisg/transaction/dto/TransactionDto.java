package ch.unisg.transaction.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;



@Data
public class TransactionDto {
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

    public TransactionDto(String merchant,String merchantCategory,String country, double amount, String currency, String cardNumber, int pin) {
        this.id = UUID.randomUUID();
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.merchantCategory = merchantCategory;
        this.country = country;
    }


}
