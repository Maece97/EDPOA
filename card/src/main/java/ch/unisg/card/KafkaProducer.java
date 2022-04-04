package ch.unisg.card;


import ch.unisg.card.dto.LimitUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class KafkaProducer {


    @Autowired
    private StreamBridge streamBridge;

    @Scheduled(cron = "*/10 * * * * *")
    public void transaction(){
        for  (int i = 0; i < 1; i++) {
            LimitUpdateDto lto = new LimitUpdateDto("123456",124);
            streamBridge.send("transaction-out-0", "kafka1",lto);
        }
    }


}