package ch.unisg.fraudpreprocessing.json;

import ch.unisg.fraudpreprocessing.dto.Transaction;
import ch.unisg.fraudpreprocessing.dto.TransactionAlert;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class TransactionAlertSerializer implements Serializer<TransactionAlert> {

    private Gson gson = new Gson();

    @Override
    public byte[] serialize(String topic, TransactionAlert myMessage) {
        if (myMessage == null) return null;
        return gson.toJson(myMessage).getBytes(StandardCharsets.UTF_8);
    }

}