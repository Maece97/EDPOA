package ch.unisg.transactionpostprocessing.serialization.avro;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import org.apache.kafka.common.serialization.Serde;

import ch.unisg.model.FilteredTransaction;
import ch.unisg.model.Transaction;

import java.util.Collections;
import java.util.Map;

public class AvroSerdes {
    
    public static Serde<Transaction> Transaction(String url, boolean isKey) {
        Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", url);
        Serde<Transaction> serde = new SpecificAvroSerde<>();
        serde.configure(serdeConfig, isKey);
        return serde;
    }

    public static Serde<FilteredTransaction> FilteredTransaction(String url, boolean isKey) {
        Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", url);
        Serde<FilteredTransaction> serde = new SpecificAvroSerde<>();
        serde.configure(serdeConfig, isKey);
        return serde;
    }

}
