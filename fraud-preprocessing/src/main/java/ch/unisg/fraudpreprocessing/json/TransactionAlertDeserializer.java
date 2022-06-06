package ch.unisg.fraudpreprocessing.json;

import ch.unisg.fraudpreprocessing.dto.Transaction;
import ch.unisg.fraudpreprocessing.dto.TransactionAlert;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class TransactionAlertDeserializer implements Deserializer<TransactionAlert> {
    private Gson gson =
            new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    @Override
    public TransactionAlert deserialize(String topic, byte[] bytes) {
        if (bytes == null) return null;
        return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), TransactionAlert.class);
    }
}
