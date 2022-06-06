package ch.unisg.exchangerates.serialization;

import ch.unisg.exchangerates.dto.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class TransactionSerializer implements Serializer<Transaction> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, Transaction transaction) {
        try {
            if(transaction==null){
                System.out.println("No data received");
                return null;
            }
            System.out.println("Serializing...");
            return objectMapper.writeValueAsBytes(transaction);


        }catch (Exception exx){
            throw new SerializationException("Error");
        }
    }
}
