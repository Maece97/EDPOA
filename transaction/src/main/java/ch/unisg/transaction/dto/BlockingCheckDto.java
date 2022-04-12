package ch.unisg.transaction.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockingCheckDto {
    private String correlationId;
    private String country;
    private String merchantCategory;
    private boolean checksPassed;

    public BlockingCheckDto(LinkedHashMap linkedHashMap){
        this.correlationId = String.valueOf(linkedHashMap.get("correlationId"));
        this.country = String.valueOf(linkedHashMap.get("country"));
        this.merchantCategory = String.valueOf(linkedHashMap.get("merchantCategory"));
        this.checksPassed = (Boolean)linkedHashMap.get("checksPassed");
    }


}
