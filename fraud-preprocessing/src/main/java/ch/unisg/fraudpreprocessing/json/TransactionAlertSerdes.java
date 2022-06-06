package ch.unisg.fraudpreprocessing.json;

import ch.unisg.fraudpreprocessing.dto.Transaction;
import ch.unisg.fraudpreprocessing.dto.TransactionAlert;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class TransactionAlertSerdes{

    public static Serde<TransactionAlert> TransactionAlert() {
        return Serdes.serdeFrom(new TransactionAlertSerializer(), new TransactionAlertDeserializer());
    }

}

