package ch.unisg.lib.logger.dto.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import ch.unisg.lib.logger.dto.Message;

import java.io.IOException;

// Custom deserializer for kafka from to convert from bytes to an object
// Normally Spring Stream uses the content-type for encoding & decoding
public class MessageDeSerializer implements Deserializer<Message<?>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message<?> deserialize(String topic, byte[] data) {
        try {
            objectMapper.findAndRegisterModules();
            return objectMapper.readValue(new String(data), Message.class);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
