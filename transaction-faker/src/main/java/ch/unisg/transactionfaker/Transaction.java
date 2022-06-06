package ch.unisg.transactionfaker;

import lombok.Data;

@Data
public class Transaction {

    String id;
    String cardNumber;
    String pin;
    String country;
    String merchant;
    String merchantCategory;
    String amount;
    String currency;
    String tries;

}
