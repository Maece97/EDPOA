package ch.unisg.exchangerates;

import ch.unisg.exchangerates.service.ExchangeRatesGetter;
import ch.unisg.exchangerates.service.ExchangeRatesUpdater;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

@SpringBootApplication
@EnableScheduling
public class ExchangeRatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRatesApplication.class, args);
		/**ExchangeRatesUpdater exchangeRatesUpdater = new ExchangeRatesUpdater();
		// Initialize exchange rate upon startup of the application --> not necessary anymore as job does it upon startup
		HashMap<String, Double> initialExchangeRates = new HashMap<>();
		initialExchangeRates.put("EUR", 1.0);
		initialExchangeRates.put("CHF", 1.0);
		initialExchangeRates.put("JPY", 1.0);
		exchangeRatesUpdater.updateExchangeRates(initialExchangeRates);**/




	}
	@Scheduled(fixedRateString = "PT1H")
	public void updateExchangeRates(){
		//Get ers from api and update them every hour
		ExchangeRatesGetter exchangeRatesGetter = new ExchangeRatesGetter();
		exchangeRatesGetter.getExchangeRates();
	}

}
