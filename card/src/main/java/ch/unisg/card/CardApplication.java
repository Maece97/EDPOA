package ch.unisg.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CardApplication {

	public static void main(String[] args) {
		System.out.println("Hi Jonas");
		SpringApplication.run(CardApplication.class, args);
	}

}
