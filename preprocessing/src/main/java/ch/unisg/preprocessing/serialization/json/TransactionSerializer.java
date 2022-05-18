package ch.unisg.preprocessing.serialization.json;

import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

import org.apache.kafka.common.serialization.Serializer;

import ch.unisg.preprocessing.dto.Transaction;

public class TransactionSerializer implements Serializer<Transaction> {
    
    private Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, Transaction myMessage) {
        if (myMessage == null) return null;
        return gson.toJson(myMessage).getBytes(StandardCharsets.UTF_8);
    }

}
