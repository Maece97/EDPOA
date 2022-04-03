package ch.unisg.lib.logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import ch.unisg.lib.logger.dto.Message;

@Component
public class Logger {

    @Autowired
    StreamBridge streamBridge;

    private org.slf4j.Logger logger;

    public Logger() {
        logger = LoggerFactory.getLogger("TEST");
    }

    public void debug(String message, Context context) {
        logger.debug(message);
        streamBridge.send("logger-out-0", new Message<String>("debugLog", message, context));
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        logger.error(message);
    }	

}
