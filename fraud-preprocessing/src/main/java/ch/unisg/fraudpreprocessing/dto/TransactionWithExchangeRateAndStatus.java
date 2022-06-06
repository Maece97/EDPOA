package ch.unisg.fraudpreprocessing.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionWithExchangeRateAndStatus extends TransactionWithExchangeRate {

    public TransactionWithExchangeRateAndStatus(TransactionWithExchangeRate transaction, String status) {
        super(transaction);
        this.status = status;
    }

    @SerializedName("status")
    String status;

}