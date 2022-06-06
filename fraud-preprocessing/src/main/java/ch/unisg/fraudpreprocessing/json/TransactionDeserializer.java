package ch.unisg.fraudpreprocessing.json;

import ch.unisg.fraudpreprocessing.dto.Transaction;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

public class TransactionDeserializer implements Deserializer<Transaction> {
    private Gson gson =
      new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

  @Override
  public Transaction deserialize(String topic, byte[] bytes) {
    if (bytes == null) return null;
    return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), Transaction.class);
  }
}
