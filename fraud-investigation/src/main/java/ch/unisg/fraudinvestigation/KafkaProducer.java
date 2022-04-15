package ch.unisg.fraudinvestigation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import ch.unisg.fraudinvestigation.domain.Transaction;
import ch.unisg.fraudinvestigation.dto.Message;
import ch.unisg.fraudinvestigation.dto.TransactionTransferObject;

@Component
public class KafkaProducer {

    @Autowired
    private StreamBridge streamBridge;

    public void createFraudDispute(Transaction t) {
        streamBridge.send("createFraudDispute-out-0", "kafka1",new Message<TransactionTransferObject>("transaction",new TransactionTransferObject(t)));
    }

}