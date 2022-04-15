package ch.unisg.model;


import ch.unisg.model.domain.Transaction;
import ch.unisg.model.dto.Message;
import ch.unisg.model.dto.TransactionTransferObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class KafkaProducer {

    private static String[] global_events = { "maintenance_begin", "maintenance_end", "plan_removed", "plan_added", "sale_begin", "sale_end" };

    @Autowired
    private StreamBridge streamBridge;

    @Scheduled(cron = "*/10 * * * * *")
    public void transaction(){
        for  (int i = 0; i < 1; i++) {
            TransactionTransferObject tto = new TransactionTransferObject(UUID.randomUUID(),new Timestamp(System.currentTimeMillis()),"Adidas",12.45,"EUR","123",456);
            streamBridge.send("transaction-out-0", "kafka1",new Message("transaction",tto));
        }
    }


}