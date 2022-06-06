package ch.unisg.card.controller;

import ch.unisg.card.dto.LimitUpdateDto;
import ch.unisg.card.dto.UpdateData;
import ch.unisg.card.service.LimitStore;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;


@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
class UpdateLimitRestController {

    private final KafkaTemplate<String,LimitUpdateDto> kafkaTemplate;


    @PostMapping("/update")
    public void startMessageProcess(@RequestBody UpdateData updateData){
        System.out.println(updateData);
        //Update Limit and publish it via Kafka
        LimitStore limitStore  = LimitStore.getInstance();
        LimitUpdateDto limitUpdateDto = new LimitUpdateDto(updateData.getCardNumber(), updateData.getLimit());
        limitStore.updateLimit(limitUpdateDto.getCardNumber(), limitUpdateDto.getLimit());
        System.out.println("Updated limit in limit store. Store currently is like:");
        System.out.println(limitStore.getLimits());
        kafkaTemplate.send("update-limit",limitUpdateDto);

        //Get card status and write to Kafka Stream
        System.out.println("Creating a Kafka producer....");
        //General Setup
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"update-status");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        //Send Message
        Producer<String, String> statusProducer = new KafkaProducer<String, String>(props);
        System.out.println("Sending new status...");
        statusProducer.send(new ProducerRecord<>("card-status", updateData.getCardNumber(),updateData.getStatus()));
    }

}