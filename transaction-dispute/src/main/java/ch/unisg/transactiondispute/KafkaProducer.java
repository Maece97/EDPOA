package ch.unisg.transactiondispute;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import ch.unisg.transactiondispute.domain.Transaction;
import ch.unisg.transactiondispute.dto.Message;
import ch.unisg.transactiondispute.dto.TransactionTransferObject;

@Component
public class KafkaProducer {

    @Autowired
    private StreamBridge streamBridge;

    public void newFraudulentTransaction(Transaction t) {
        streamBridge.send("fraudulentTransaction-out-0", "kafka1",new Message<TransactionTransferObject>("transaction",new TransactionTransferObject(t)));
    }

}