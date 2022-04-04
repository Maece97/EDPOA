package ch.unisg.card.dto.converters;

import ch.unisg.card.dto.LimitUpdateDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.io.OutputStream;

public class Serializer implements org.apache.kafka.common.serialization.Serializer<LimitUpdateDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, LimitUpdateDto data) {
        try {
            objectMapper.findAndRegisterModules();
            return objectMapper.writeValueAsBytes(data);

        }catch (JsonProcessingException e){
            throw new SerializationException(e);
        }

    }
}
