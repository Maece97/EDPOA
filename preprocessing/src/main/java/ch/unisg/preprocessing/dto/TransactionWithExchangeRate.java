package ch.unisg.preprocessing.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionWithExchangeRate extends Transaction {

    public TransactionWithExchangeRate(Transaction transaction, String exchangeRate) {
        super(transaction);
        this.exchangeRate = exchangeRate;
    }

    TransactionWithExchangeRate(TransactionWithExchangeRate transaction) {
        super(transaction);
        this.exchangeRate = transaction.getExchangeRate();
    }

    @SerializedName("exchangeRate")
    String exchangeRate;

}