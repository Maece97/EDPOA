package ch.unisg.pin;

import ch.unisg.pin.dto.CamundaMessageDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class PinApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinApplication.class, args);
	}

	@Bean
	public Consumer<CamundaMessageDto> pin(){
		System.out.println("Got something");
		return null;
	}

}
