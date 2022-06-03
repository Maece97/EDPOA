package ch.unisg.preprocessing.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    Transaction(Transaction transaction) {
        this.id = transaction.getId();
        this.cardNumber = transaction.getCardNumber();
        this.pin = transaction.getPin();
        this.country = transaction.getCountry();
        this.merchant = transaction.getMerchant();
        this.merchantCategory = transaction.getMerchantCategory();
        this.amount = transaction.getAmount();
        this.currency = transaction.getCurrency();
        this.tries = transaction.getTries();
    }

    @SerializedName("id")
    String id;

    @SerializedName("cardNumber")
    String cardNumber;

    @SerializedName("pin")
    String pin;

    @SerializedName("country")
    String country;

    @SerializedName("merchant")
    String merchant;

    @SerializedName("merchantCategory")
    String merchantCategory;

    @SerializedName("amount")
    String amount;

    @SerializedName("currency")
    String currency;

    @SerializedName("tries")
    String tries;



}