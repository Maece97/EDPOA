package ch.unisg.preprocessing.serialization.json;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import ch.unisg.preprocessing.dto.Transaction;

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
