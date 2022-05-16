package ch.unisg.exchangerates.controller;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Properties;


@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
class UpdateExchangeRateRestController {

    private final KafkaTemplate<String,Double> kafkaTemplate;


    @PostMapping("/update")
    public void startMessageProcess(@RequestBody String rate){

        System.out.println("Creating producer");

        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"client");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        Producer<String, String> transactionProd = new KafkaProducer<String, String>(props);
        System.out.println("Start sending messages");
        for (int i = 0 ;i<20;i++){
            System.out.println("Sending message");
            producer.send(new ProducerRecord<>("exchange-rates","alice"+i,rate));
        }

        System.out.println("Sending a transaction here");
        transactionProd.send(new ProducerRecord<>("incoming-transactions","hi", "lo"));

        System.out.println("Finished sending message");
        producer.close();


    }


    }

