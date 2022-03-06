package ch.unisg.model;

import ch.unisg.model.dto.Message;
import ch.unisg.model.dto.TransactionTransferObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

@SpringBootApplication
@EnableScheduling
public class ModelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModelApplication.class, args);
	}

	@Bean
	public Consumer<Message<?>> transaction(){return this::randomChoice;}

	public void randomChoice (Message<?> message){
		LinkedHashMap transactionData = (LinkedHashMap) message.getData();
		System.out.println(transactionData);

}}
