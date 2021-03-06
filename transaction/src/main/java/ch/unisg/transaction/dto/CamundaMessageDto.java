package ch.unisg.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CamundaMessageDto implements Serializable {

    private String correlationId;
    private PinCheckDto dto;

}
