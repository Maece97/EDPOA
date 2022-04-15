package ch.unisg.fraudinvestigation.dto;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.UUID;

import ch.unisg.fraudinvestigation.domain.Transaction;
import lombok.Getter;
import lombok.Setter;

public class TransactionTransferObject {
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

    public TransactionTransferObject(){};
    public TransactionTransferObject(UUID id, Timestamp timestamp, String merchant, double amount, String currency, String cardNumber, int pin) {
        this.id = id;
        this.timestamp = timestamp;
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public TransactionTransferObject(LinkedHashMap map){
        this.setAmount((double)map.get("amount"));
        this.setCurrency((String) map.get("currency"));
        this.setCardNumber((String)map.get("cardNumber"));
        this.setId(UUID.fromString((String)map.get("id")));
        this.setMerchant((String)map.get("merchant"));
        this.setTimestamp(new Timestamp((long)map.get("timestamp")));
        this.setPin((int) map.get("pin"));
    }

    public TransactionTransferObject(Transaction transaction){
        this.setAmount(transaction.getAmount());
        this.setCurrency(transaction.getCurrency());
        this.setCardNumber(transaction.getCardNumber());
        this.setId(transaction.getId());
        this.setMerchant(transaction.getMerchant());
        this.setTimestamp(transaction.getTimestamp());
        this.setPin(transaction.getPin());
    }

}