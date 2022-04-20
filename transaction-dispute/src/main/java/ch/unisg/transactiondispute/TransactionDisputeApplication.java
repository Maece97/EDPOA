package ch.unisg.transactiondispute;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.unisg.transactiondispute.domain.Transaction;
import ch.unisg.transactiondispute.dto.Message;
import ch.unisg.transactiondispute.dto.TransactionTransferObject;

@SpringBootApplication
public class TransactionDisputeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionDisputeApplication.class, args);
	}

	@Bean
	public Consumer<Message<?>> createFraudDispute() {
		return this::newFraudDispute;
	}

	@Autowired
    private KafkaProducer kafkaProducer;

	public void newFraudDispute (Message<?> message){
		System.out.println("New fraud dispute!");
		// Needs to be converted "manually" since the deserializer always returns a HashMap
		TransactionTransferObject tto = new TransactionTransferObject(((LinkedHashMap)message.getData()));
		Transaction transaction = new Transaction(tto, kafkaProducer);
		transaction.handleDispute();
	}

}
