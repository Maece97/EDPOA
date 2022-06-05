package ch.unisg.fraudpreprocessing.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAlert {

    TransactionAlert(TransactionAlert transactionAlert){
        this.count = transactionAlert.count;
    }

    @SerializedName("count")
    String count;
}
