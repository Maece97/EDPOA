package ch.unisg.preprocessing.serialization.json;

import ch.unisg.preprocessing.dto.Transaction;
import ch.unisg.preprocessing.dto.TransactionWithExchangeRate;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class TransactionWithErSerdes implements Serde<TransactionWithExchangeRate> {
    
    @Override
    public Serializer<TransactionWithExchangeRate> serializer() {
        return new TransactionWithErSerializer();
    }
    
    @Override
    public Deserializer<TransactionWithExchangeRate> deserializer() {
        return new TransactionWithErDeserializer();
    }

}
