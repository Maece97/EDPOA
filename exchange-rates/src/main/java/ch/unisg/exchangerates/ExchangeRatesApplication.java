package ch.unisg.exchangerates;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

@SpringBootApplication
public class ExchangeRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRatesApplication.class, args);
		// Initialize exchange rate upon startup of the application
		HashMap<String, Double> initialExchangeRates = new HashMap<>();
		initialExchangeRates.put("EUR", 0.23);
		initialExchangeRates.put("CHF", 0.79);
		initialExchangeRates.put("JPY", 1.78);
		//Send them to the preprocessing app
		Properties props = new Properties();
		props.put(ProducerConfig.CLIENT_ID_CONFIG,"client");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.DoubleSerializer");

		Producer<String, Double> producer = new KafkaProducer<String, Double>(props);
		System.out.println("Start sending messages");
		System.out.println("Sending message");
		Set<String> keys = initialExchangeRates.keySet();
		for(String key : keys){
			double exchangeRate = initialExchangeRates.get(key);
			producer.send(new ProducerRecord<>("exchange-rates", key,exchangeRate));
		}



		System.out.println("Finished sending message");
		producer.close();


	}

}
