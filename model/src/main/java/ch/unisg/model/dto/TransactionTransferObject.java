package ch.unisg.model.dto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.UUID;

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
        TransactionTransferObject tto = new TransactionTransferObject();
        tto.setAmount((double)map.get("amount"));
        tto.setCurrency((String) map.get("currency"));
        tto.setCardNumber((String)map.get("cardNumber"));
        tto.setId(UUID.fromString((String)map.get("id")));
        tto.setMerchant((String)map.get("merchant"));
        tto.setTimestamp(new Timestamp((long)map.get("timestamp")));
        tto.setPin((int) map.get("pin"));
    }

}