package ch.unisg.fraudinvestigation;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.unisg.fraudinvestigation.domain.Transaction;
import ch.unisg.fraudinvestigation.dto.Message;
import ch.unisg.fraudinvestigation.dto.TransactionTransferObject;

@SpringBootApplication
public class FraudInvestigationApplication {

	public static void main(String[] args) {
		SpringApplication.run(FraudInvestigationApplication.class, args);
	}

	@Bean
	public Consumer<Message<?>> possibleFraudTransaction() {
		return this::acceptOrDecline;
	}

	@Autowired
    private KafkaProducer kafkaProducer;

	public void acceptOrDecline (Message<?> message){
		System.out.println("POSSIBLE FRAUD DETECTED!");
		// Needs to be converted "manually" since the deserializer always returns a HashMap
		TransactionTransferObject tto = new TransactionTransferObject(((LinkedHashMap)message.getData()));
		Transaction transaction = new Transaction(tto, kafkaProducer);
		transaction.acceptOrDecline();
	}

}
