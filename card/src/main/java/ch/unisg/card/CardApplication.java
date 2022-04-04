package ch.unisg.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CardApplication {

	public static void main(String[] args) {
		System.out.println("Hello there");
		SpringApplication.run(CardApplication.class, args);
	}

}
