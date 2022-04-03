package ch.unisg.model;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import ch.unisg.lib.logger.Context;
import ch.unisg.lib.logger.Logger;

@SpringBootApplication
@ComponentScan({"ch.unisg.lib.logger", "ch.unisg.model"})
public class ModelApplication {

	@Autowired
	private Logger logger;

	public static void main(String[] args) {
		SpringApplication.run(ModelApplication.class, args);

	}

	@PostConstruct
    public void listen() { 
        Context context = Context.createNewContextFactory("modelService");
		logger.debug("HELLO FROM DEBUG", context);
    }

}
