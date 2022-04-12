package ch.unisg.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PinCheckDto implements Serializable {

    private String correlationId;
    private String cardNumber;
    private String pin;
    private boolean pinCorrect;

    public PinCheckDto(LinkedHashMap linkedHashMap){
        this.correlationId = String.valueOf(linkedHashMap.get("correlationId"));
        this.cardNumber  = String.valueOf(linkedHashMap.get("cardNumber"));
        this.pin = String.valueOf(linkedHashMap.get("pin"));
        this.pinCorrect = Boolean.valueOf((Boolean) linkedHashMap.get("pinCorrect"));
    }


}
