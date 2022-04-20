package ch.unisg.model;


import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.unisg.model.domain.Transaction;
import ch.unisg.model.dto.Message;
import ch.unisg.model.dto.TransactionTransferObject;

@Component
public class KafkaProducer {

    @Autowired
    private StreamBridge streamBridge;

    // @Scheduled(cron = "*/10 * * * * *")
    // public void transaction(){
    //     for  (int i = 0; i < 1; i++) {
    //         TransactionTransferObject tto = new TransactionTransferObject(UUID.randomUUID(),new Timestamp(System.currentTimeMillis()),"Adidas",12.45,"EUR","123",456);
    //         streamBridge.send("transaction-out-0", "kafka1",new Message<TransactionTransferObject>("transaction",tto));
    //     }
    // }

    public void possibleFraudDetected(Transaction t) {
        streamBridge.send("possibleFraudTransaction-out-0", "kafka1",new Message<TransactionTransferObject>("transaction",new TransactionTransferObject(t)));
    }

}