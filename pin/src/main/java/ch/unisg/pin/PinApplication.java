package ch.unisg.pin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PinApplication {

	public static void main(String[] args) {
		System.out.println("Jonas");
		SpringApplication.run(PinApplication.class, args);
	}

}
