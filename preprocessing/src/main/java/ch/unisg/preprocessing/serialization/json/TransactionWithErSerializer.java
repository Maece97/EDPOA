package ch.unisg.preprocessing.serialization.json;

import ch.unisg.preprocessing.dto.Transaction;
import ch.unisg.preprocessing.dto.TransactionWithExchangeRate;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class TransactionWithErSerializer implements Serializer<TransactionWithExchangeRate> {
    
    private Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, TransactionWithExchangeRate myMessage) {
        if (myMessage == null) return null;
        return gson.toJson(myMessage).getBytes(StandardCharsets.UTF_8);
    }

}
