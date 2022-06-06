package ch.unisg.model;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import ch.unisg.model.domain.Transaction;
import ch.unisg.model.dto.Message;

@SpringBootApplication
@EnableScheduling
public class ModelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModelApplication.class, args);
	}

	@Bean
	public Consumer<FilteredTransaction> transaction() {
		System.out.println("-----> New transaction. Checking if it's fraud...");
		return this::acceptOrDecline;
	}

	@Autowired
    private KafkaProducer kafkaProducer;

	public void acceptOrDecline (FilteredTransaction message){
		Transaction transaction = new Transaction(message, kafkaProducer);
		transaction.acceptOrDecline();
	}

	@Bean
	public Consumer<Message<?>> fraudulentTransaction(){return this::newFraudulentTransactionTransaction;}

	public void newFraudulentTransactionTransaction (Message<?> message){
		// TODO use this information to retrain the model
		System.out.println("-----> New fraud transaction received! Retraining the model......");
	}
}
