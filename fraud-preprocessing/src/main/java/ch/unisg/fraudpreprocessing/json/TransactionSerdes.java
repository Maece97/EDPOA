package ch.unisg.fraudpreprocessing.json;

import ch.unisg.fraudpreprocessing.dto.Transaction;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class TransactionSerdes implements Serde<Transaction> {
    
    @Override
    public Serializer<Transaction> serializer() {
        return new TransactionSerializer();
    }
    
    @Override
    public Deserializer<Transaction> deserializer() {
        return new TransactionDeserializer();
    }

}
