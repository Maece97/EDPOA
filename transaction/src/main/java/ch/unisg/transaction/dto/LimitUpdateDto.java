package ch.unisg.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LimitUpdateDto {
    private String cardNumber;
    private int limit;
}
