package ch.unisg.exchangerates.controller;


import ch.unisg.exchangerates.dto.Transaction;
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
    public void startMessageProcess(@RequestBody String body){
        System.out.println(body);
        String [] splitted = body.split(",");
        String rate = splitted[0].substring(13);
        Double exchange_rate = Double.valueOf(rate);
        System.out.println(rate);
        String currency = splitted[1].substring(17,20);
        System.out.println(currency);

        System.out.println("Creating producer");

        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"client");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.DoubleSerializer");

        Producer<String, Double> producer = new KafkaProducer<String, Double>(props);
        System.out.println("Start sending messages");
            System.out.println("Sending message");
            producer.send(new ProducerRecord<>("exchange-rates", currency,exchange_rate));



        System.out.println("Finished sending message");
        producer.close();


    }


    }

