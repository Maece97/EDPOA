package ch.unisg.lib.logger.dto.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import ch.unisg.lib.logger.dto.Message;

// Custom deserializer for kafka from to convert from an object to bytes
public class MessageSerializer implements Serializer<Message<?>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Message<?> data) {
        try {
            objectMapper.findAndRegisterModules();
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }

    }
}

