package ch.unisg.blockingrules.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockingCheckDto {
    private String correlationId;
    private String country;
    private String merchantCategory;
    private boolean checksPassed;


}
