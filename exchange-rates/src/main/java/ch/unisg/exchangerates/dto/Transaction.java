package ch.unisg.exchangerates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction  {
    String cardNumber;
    int amount;
    String currency;

}
