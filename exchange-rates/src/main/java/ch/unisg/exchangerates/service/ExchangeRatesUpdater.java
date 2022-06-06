package ch.unisg.exchangerates.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class ExchangeRatesUpdater {
    //sending updated exchange rates to preprocessing service
    public void updateExchangeRates(HashMap<String,Double> exchangeRates){
        //Send them to the preprocessing app
        System.out.println("###UPDATING EXCHANGE RATES###");
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"client");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.DoubleSerializer");

        Producer<String, Double> producer = new KafkaProducer<String, Double>(props);
        System.out.println("Start sending messages");
        System.out.println("Sending message");
        Set<String> keys = exchangeRates.keySet();
        for(String key : keys){
            double exchangeRate = exchangeRates.get(key);
            producer.send(new ProducerRecord<>("exchange-rates", key,exchangeRate));
        }

        System.out.println("###FINISHED UPDATING EXCHANGE RATES");
        producer.close();


    }
}
