package ch.unisg.fraudpreprocessing.json;

import java.sql.Timestamp;

import ch.unisg.fraudpreprocessing.dto.Transaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class TransactionTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime){
        var transaction = (Transaction) record.value();
        if(transaction != null && transaction.getTimestamp() != null){
            var timestamp = transaction.getTimestamp();
            var millis = Timestamp.valueOf(timestamp).toInstant().toEpochMilli();
            return millis;
        }

        return partitionTime;
    }
}
