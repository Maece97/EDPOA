package ch.unisg.transactiondispute.dto;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.UUID;

import ch.unisg.transactiondispute.domain.Transaction;
import lombok.Data;

@Data
public class TransactionTransferObject {
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

    public TransactionTransferObject(){};
    public TransactionTransferObject(UUID id, Timestamp timestamp, String merchant, double amount, String currency, 
        String cardNumber, int pin, String merchantCategory, String country) {
        this.id = id;
        this.timestamp = timestamp;
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.merchantCategory = merchantCategory;
        this.country = country;
    }

    public TransactionTransferObject(LinkedHashMap map){
        this.setAmount((double)map.get("amount"));
        this.setCurrency((String) map.get("currency"));
        this.setCardNumber((String)map.get("cardNumber"));
        this.setId(UUID.fromString((String)map.get("id")));
        this.setMerchant((String)map.get("merchant"));
        this.setTimestamp(new Timestamp((long)map.get("timestamp")));
        this.setPin((int) map.get("pin"));
        this.setMerchantCategory((String) map.get("merchantCategory"));
        this.setCountry((String) map.get("country"));
    }

    public TransactionTransferObject(Transaction transaction){
        this.setAmount(transaction.getAmount());
        this.setCurrency(transaction.getCurrency());
        this.setCardNumber(transaction.getCardNumber());
        this.setId(transaction.getId());
        this.setMerchant(transaction.getMerchant());
        this.setTimestamp(transaction.getTimestamp());
        this.setPin(transaction.getPin());
        this.merchantCategory = transaction.getMerchantCategory();
        this.country = transaction.getCountry();
    }

}